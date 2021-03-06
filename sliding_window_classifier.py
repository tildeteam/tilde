#!/usr/bin/python
""" Sliding window classifier

"""

"""
TODO:
1. positive/negative learn option?
2. multiprocessing ?
"""
__version__ = "1.2"

from argparse import ArgumentParser, RawTextHelpFormatter
import os
import sys
import json
import logging
import math
import numpy as np
import multiprocessing as mp
from enum import Enum
import time

# default constants
NON_COMPUTABLE = 0
FEATURE_DELIMITER = ','
PEARSON_THRESHOLD_DEFAULT = 0.825
WINDOW_SIZE_DEFAULT = 100
LEARN_RATE_DEFAULT = 0.2
DEFAULT_WEIGHT_OUTPUT_LOCATION = "sliding_window_classifier_weights.json"
DEBUG_PRINTS_DEFAULT = False
APPROXIMATE_SINGULAR_MATRIX_DEFAULT = False
ITERATIONS_NUM_OF_DEFAULT = 1
SPLIT_DATA_TO_ROWS = 2000

# enums
PEARSON_THRESHOLD_MIN = 0
PEARSON_THRESHOLD_MAX = 1


class SlidingWindowStatus(Enum):
    ANOMALY_NOT_FOUND = 0
    ANOMALY_FOUND = 1
    WINDOW_SIZE_DOES_NOT_MEET_REQUIREMENTS = 2

# configurations
MODE_TRAIN = "train"
MODE_TEST = "test"

AVAILABLE_MODES = [MODE_TEST, MODE_TRAIN]


class SlidingWindow(object):

    def __init__(self, mode, learn_rate, window_size, pearson_threshold, feature_weights, input_file, debug,
                 approximate):
        self.mode = mode
        self.learn_rate = learn_rate
        self.window_size = window_size
        self.pearson_threshold = pearson_threshold
        self.feature_weights = feature_weights

        self.input_file = input_file
        self.approximate = approximate
        self.window = []
        # shm variables
        self.task_queue_in = None
        self.task_queue_out = None
        # statistics
        self.singular_matrices_num_of = 0
        self.fp_num_of = 0
        self.tp_num_of = 0
        self.fn_num_of = 0
        self.tn_num_of = 0

        logging.basicConfig(filename="sliding_window_w{0}_p{1}_{2}.log".format(self.window_size, self.pearson_threshold, "no_approximation" if not self.approximate else "approximation"), level=logging.DEBUG, filemode="w")
        self.logger = logging.getLogger("Sliding Window")
        handler = logging.StreamHandler(sys.stdout)
        handler.setLevel(logging.DEBUG if debug else logging.INFO)
        self.logger.addHandler(handler)
        self.logger.info("Sliding window init")

    @staticmethod
    def average(x):
        """

        :param x: 1-d numpy array
        :type x: np.array
        :return:
        """
        assert len(x) > 0
        return np.average(x)

    @staticmethod
    def pearson_def(x, y):
        """ Calculates pearson coefficient correlation between x and y.

        :param x: float vector
        :type x: iterable
        :param y: float vector
        :type y: iterable
        :return: value between -1 and 1
        :rtype: float
        """
        assert len(x) == len(y), "Got instances of different length. Len(x)={0}, Len(y)={1}".format(len(x), len(y))
        n = len(x)
        assert n > 0, "Got instance of Len=0"
        avg_x = SlidingWindow.average(x)
        avg_y = SlidingWindow.average(y)
        diffprod = 0
        xdiff2 = 0
        ydiff2 = 0
        for idx in range(n):
            xdiff = x[idx] - avg_x
            ydiff = y[idx] - avg_y
            diffprod += xdiff * ydiff
            xdiff2 += xdiff * xdiff
            ydiff2 += ydiff * ydiff

        denominator = math.sqrt(xdiff2 * ydiff2)
        if not denominator:
            return 0.0
        return diffprod / denominator

    def mahalanobis_distance(self, vec, mat, approximate=APPROXIMATE_SINGULAR_MATRIX_DEFAULT):
        """ Calculate mahalanobis distance from 1xn vec to mxn mat

        :param vec: 1xn matrix, where n represents number of attributes
        :type mat: np.ndarray
        :param mat: mxn matrix, where m represents number of rows, n represents number of attributes
        :type mat: np.ndarray
        :param approximate: should approximation be done in case of singular matrix
        :type approximate: bool
        :return: mahalanobis distance or NON_COMPUTABLE
        :rtype: float
        """
        m_mat = np.matrix(mat) if not isinstance(mat, np.matrixlib.defmatrix.matrix) else mat
        cov_m_mat = np.cov(m_mat.T)
        try:
            inv_cov_mat = np.linalg.inv(cov_m_mat)
        except np.linalg.LinAlgError:  # singular matrix
            self.singular_matrices_num_of += 1  # collect statistics regardless of approximation mode
            if not approximate:
                return NON_COMPUTABLE
            inv_cov_mat = np.linalg.pinv(cov_m_mat)
        m_vec = np.matrix(vec) if not isinstance(vec, np.matrixlib.defmatrix.matrix) else vec
        means = m_mat.mean(axis=0)
        d_from_centroid = m_vec - means
        return np.nan_to_num(np.sqrt((d_from_centroid.dot(inv_cov_mat).dot(d_from_centroid.transpose())).item()))

    def _build_correlation_sets(self):
        corr_mat = np.nan_to_num(np.corrcoef(self.window.T))
        assigned_cols = set()  # keeps track of already assigned cols, so finish at len(assigned_cols) == len(cols)
        cs = []
        for i in range(len(corr_mat)):
            if i in assigned_cols:
                continue
            cs.append(list())
            cs[-1].append(i)
            assigned_cols.add(i)
            # corr_mat is symmetric -> iterate upper half
            for j in filter(lambda ix: ix not in assigned_cols, range(i + 1, len(corr_mat))): #TODO check whether filter is needed
                if abs(corr_mat[i][j]) >= self.pearson_threshold:
                    cs[-1].append(j)
                    assigned_cols.add(j)
            if len(cs[-1]) > 1:  # log only relevant correlation sets
                self.logger.debug("Added correlation set:{0}".format(cs[-1]))
        self.logger.info("Number of correlation sets: {0}. Largest set size: {1}. Smallest set size: {2}".
                         format(len(cs), max(map(lambda s: len(s), cs)), min(map(lambda s: len(s), cs))))
        self.logger.debug("Correlation sets (with more than 1 element): {0}".format(
                    list(filter(lambda cs_i: len(cs_i) > 1, cs))))
        # remove correlation sets of len 1
        return list(filter(lambda cs_i: len(cs_i) > 1, cs))

    def _build_correlation_sets_thresholds(self, cs):
        ts = []
        # instead of deleting vec_j in each iteration, mask it out of the array (slice the np.Array)
        row_mask = np.ones((self.window_size,), bool)
        # each cs_i's max M.D. is calculated by a different worker
        # requirements:
        # 1. window in shm as mp.RawArray(c_double, sizeof(window))
        # 2. CSs in shm as mp.Manager.list
        # 3. idx is the only thing sent to the mp.Queue
        for i, cs_i in enumerate(cs):
            if len(cs_i) <= 1:
                # skip CSi if number of attributes monitored <= 1 (calculation of M.D. of dim(1) is irrelevant)
                continue
            max_md = -1
            for j in range(self.window_size):
                row_mask[j] = False  # mask vec_j
                # remove vec j and slice cols according to CSi
                window_without_vec_j = self.window[row_mask, :][:, cs_i]
                # vec_j with sliced cols according to CSi
                vec_j = self.window[j, cs_i]
                max_md = max(self.mahalanobis_distance(vec_j, window_without_vec_j), max_md)
                row_mask[j] = True  # unmask vec_j for next iteration
            ts.append((i, max_md))
            self.logger.debug("Correlation set {0}'s threshold: {1}".format(i, max_md))
        self.logger.info("Correlation sets' thresholds: {0}".format(ts))

        return ts

    def trainer(self):
        cs = self._build_correlation_sets()
        ts = self._build_correlation_sets_thresholds(cs)

        return cs, ts

    def run(self, anomaly_callback=None):
        """ Runs the sliding window classifier using requested parameters

        :param anomaly_callback: cb when anomaly is found. should receive (int, )
        :type anomaly_callback: function
        :return: tuple of (status code represented as enum, [#TP, #FP])
        :rtype: tuple(SlidingWindowStatus, list)
        """
        self.logger.info("Running sliding window with params: mode={mode}, "
                         "learn_rate={learn_rate}, window_size={window_size}, "
                         "pearson_threshold={pearson_threshold}, input_file={input_file}".
                         format(mode=self.mode, learn_rate=self.learn_rate, window_size=self.window_size,
                                pearson_threshold=self.pearson_threshold, input_file=self.input_file))
        if self.mode == MODE_TEST or self.feature_weights.cs:
            self.logger.info("Pretrained weights: {0}".format(self.feature_weights.cs))

        #self.logger.info("Spawning {0} workers".format(10))

        #from contextlib import closing
        #with closing(mp.Pool(processes=10, initializer=_initalizer, initargs=(self.window,
        #                                                                     self.task_queue_in,
        #                                                                      self.task_queue_out))) as #pool:
        #    #pool.map(None, None)
        #    pass
        #pool.join()


        with open(self.input_file, 'r') as input_stream:
            # -- fill the window first --
            for i, v in enumerate(input_stream):
                if i == self.window_size:
                    break
                #v = v.split(FEATURE_DELIMITER) if self.mode == MODE_TEST else v.split(FEATURE_DELIMITER)[:-1]
                v = v.split(FEATURE_DELIMITER)[:-1]
                self.window.append(list(map(lambda x: int(x), v)))
            else:  # did not break - input file too small for window
                return SlidingWindowStatus.WINDOW_SIZE_DOES_NOT_MEET_REQUIREMENTS

            # from now on, window will be treated as np.array
            self.window = np.asarray(self.window)
            # -- build correlation sets and thresholds on filled window --
            # note that sets and thresholds are already filtered to > sets of len 1
            # to remove redundant M.D calculation
            cs, ts = self.trainer()
            # -- classify new instances --
            # for each new instance, calc M.D. of sliced cols according to CSi.
            # M.D.i > TSi * weight --> classify as anomaly.
            # on train mode -> update weights
            # on test mode -> classify and continue(or exit)
            # continue such that the new instance replaces the first instance (self.window[0])
            # and next, build the new TS and CS.
            for ix, v in enumerate(input_stream, start=self.window_size):
                start_time = time.time()
                tmp_v = v.split(FEATURE_DELIMITER)
                v = tmp_v[:-1]
                v_class = int(tmp_v[-1])

                new_vec = np.array(list(map(lambda x: int(x), v)))
                declared_as_anomaly = False
                # calc M.D per CSi. on singular matrices we skip the anomaly testing
                for cs_i, (i, ts_i) in zip(cs, ts):
                    if ts_i == 0:
                        continue					
                    md_on_cs_i = self.mahalanobis_distance(new_vec[cs_i], self.window[:, cs_i])
                    self.logger.debug("ix: {0}. Calculated M.D. {1} of CS ix {2} with threshold {3}".
                                      format(ix, md_on_cs_i, i, ts_i))
                    if md_on_cs_i == NON_COMPUTABLE:
                        continue
                    if md_on_cs_i > ts_i * self.feature_weights.get_weight_of(cs_i):
                        if anomaly_callback:
                            anomaly_callback(ix)
                        declared_as_anomaly = True
                        self.logger.critical("ix: {0}. Anomaly found. M.D. {1} on CS ix {2}. TS:{3}. CS weight: {4}".
                                             format(ix, md_on_cs_i, i, ts_i, self.feature_weights.get_weight_of(cs_i)))
                        # anomaly found and we're in test mode, other correlation sets are not needed to be tested
                        if self.mode == MODE_TEST:
                            break
                        # on train mode - we know the classification, so evaluation is done and we update weights.
                        # Note that class attribute MUST be saved as the last field
                        if self.mode == MODE_TRAIN:
                            # TP -> train with positive value (1), otherwise we consider the classification as:
                            # FP -> train with negative value (-1)
                            classification = "TP" if v_class == 1 else "FP"
                            if classification == "FP":
							    self.feature_weights.set_weight_of(cs_i,
																   self.feature_weights.get_weight_of(cs_i) +
																   self.learn_rate * (-1 if classification == "TP" else 1))
							    self.logger.critical("ix: {0} Anomaly is evaluated as {1}. Updating weights of"
													 " CS ix {2} to: {3}".
													 format(ix, classification, i,
															self.feature_weights.get_weight_of(cs_i)))
                    elif self.mode == MODE_TRAIN:
                        # TN -> train with positive value(1), otherwise we consider the classification as:
                        # FN -> train with negative value (-1)
                        classification = "FN" if v_class == 1 else "TN"
                        if classification == "FN":
							self.feature_weights.set_weight_of(cs_i,
															   self.feature_weights.get_weight_of(cs_i) +
															   self.learn_rate * (1 if classification == "TN" else -1))
							self.logger.critical("ix: {0} Anomaly is evaluated as {1}. Updating weights of"
												 " CS ix {2} to: {3}".
												 format(ix, classification, i,
														self.feature_weights.get_weight_of(cs_i)))
                # -- collect statistics overall on all CSets (only in train mode we have the class attribute) --
                if self.mode == MODE_TEST or self.mode == MODE_TRAIN:
                    if declared_as_anomaly:
                        if v_class == 1:  # TP
                            self.tp_num_of += 1
                        else:  # FP
                            self.fp_num_of += 1
                    else:
                        if v_class == 1:  # FN
                            self.fn_num_of += 1
                            self.logger.critical("ix: {0} was skipped but is a true anomaly (FN)".format(ix))
                        else:  # TN
                            self.tn_num_of += 1

                # slide the window - note that window size at all times leaks up to 1*sizeof(instance)
                self.window = np.vstack((self.window, new_vec))
                self.window = self.window[1:]
                # recalculate cs, ts based on the new window
                cs, ts = self.trainer()
                self.logger.info("Iteration: {0} took {1:.2f}s".format(ix, time.time() - start_time))
            if self.mode == MODE_TRAIN:
                self.logger.info("Saving weights to output file")
                self.feature_weights.save()
            # -- statistics --
            self.logger.info("\n\n********** Statistics ********** ")
            self.logger.info("Number of singular matrices calculations: {0}".format(self.singular_matrices_num_of))
            self.logger.info("TP/FP/TN/FN: {0}/{1}/{2}/{3}".format(self.tp_num_of, self.fp_num_of,
                                                                    self.tn_num_of, self.fn_num_of))
            # cleanup - TODO: move to cleanup function
            for handler in self.logger.handlers:
                handler.close()
                self.logger.removeHandler(handler)


class FeatureWeights(object):

    def __init__(self, output_file=DEFAULT_WEIGHT_OUTPUT_LOCATION):
        self.cs = []
        self.output_file = output_file

    @classmethod
    def from_json(cls, input_file, output_file=DEFAULT_WEIGHT_OUTPUT_LOCATION):
        feature_weights = cls(output_file)

        with open(input_file, 'r') as f:
            data = json.load(f)
        try:
            for cs in data:
                feature_weights.cs.append(
                    {
                        "feature_indices": cs["feature_indices"],
                        "weight": cs["weight"]
                    }
                )
        except KeyError:
            raise TypeError("Input file {0} is not in the correct format. Please check parser help section".
                            format(input_file))

        return feature_weights

    def save(self):
        with open(self.output_file, 'w') as f:
            json.dump(self.cs, f, indent=4)

    def get_weight_of(self, cs, default=1):
        tup = list(filter(lambda i: i["feature_indices"] == cs, self.cs))
        if not tup:
            return default
        return tup[0]["weight"]

    def set_weight_of(self, cs, weight):
        tup = list(filter(lambda i: i["feature_indices"] == cs, self.cs))
        if not tup:  # new cs
            self.cs.append(
                {
                    "feature_indices": cs,
                    "weight": weight
                }
            )
            return 1
        tup[0]["weight"] = weight
        return 0


def _initalizer(window, task_queue_in, task_queue_out):
    # shm variables as global in contexts of sub-processes
    global shared_window
    global inq
    global outq
    shared_window = window
    inq = task_queue_in
    outq = task_queue_out


def main():
    opt_parse()


def opt_parse():
    parser = ArgumentParser(usage='%(prog)s [options] [{modes}] [input file]'.format(modes="|".join(AVAILABLE_MODES)),
                            formatter_class=RawTextHelpFormatter,
                            description="""
Sliding window classifier
Templates:

    Run train mode on your data with default values:
        %(prog)s train -o weights.json

    Run in 100 iterations, while feeding and training the same weights (using default values):
        First iteration (to create the initial weights file you need to run this, or create a weights file manually):
            %(prog)s train -o weights.json
        Other 99 iterations:
            %(prog)s train -t weights.json -o weights.json -i 99
""")
    parser.add_argument('parameters', type=str, nargs=2, help='[{modes}] [input file]'.
                        format(modes="|".join(AVAILABLE_MODES)))
    parser.add_argument('-l', '--learn-rate', action='store', dest='learn_rate', type=float, metavar='FLOAT_VAL',
                        help='The amount in which increase/decrease is done on TP/FP instances. Default: {0}'.
                        format(LEARN_RATE_DEFAULT),
                        default=LEARN_RATE_DEFAULT)
    parser.add_argument('-i', '--iterations', action='store', dest='iterations', type=int, metavar='INT_VAL',
                        help='Number of iterations on input file(in train mode only!). '
                             'Should be used in conjunction with'
                             'pretrained weights file as output weights file. Default: {0}'.
                        format(ITERATIONS_NUM_OF_DEFAULT),
                        default=ITERATIONS_NUM_OF_DEFAULT)
    parser.add_argument('-d', '--debug', action='store_true', dest='debug',
                        help='Should print debug logs to standard output. Default: {0}'.format(DEBUG_PRINTS_DEFAULT),
                        default=DEBUG_PRINTS_DEFAULT)
    parser.add_argument('-a', '--approximate', action='store_true', dest='approximate',
                        help='Should approximate singular matrices. Default: {0}'.
                        format(APPROXIMATE_SINGULAR_MATRIX_DEFAULT),
                        default=APPROXIMATE_SINGULAR_MATRIX_DEFAULT)
    parser.add_argument('-s', '--window-size', action='store', dest='window_size', type=int,
                        help='Window size to maintain. Default: {0}'.format(WINDOW_SIZE_DEFAULT),
                        default=WINDOW_SIZE_DEFAULT, metavar='INT_VAL')
    parser.add_argument('-p', '--pearson-threshold', action='store', dest='pearson_threshold', type=float,
                        help='Correlation threshold parameter (Range: {1}..{2}). '
                             'This parameter governs the size of the correlation attributes set. Default: {0}'.
                        format(PEARSON_THRESHOLD_DEFAULT, PEARSON_THRESHOLD_MIN, PEARSON_THRESHOLD_MAX),
                        default=PEARSON_THRESHOLD_DEFAULT, metavar='FLOAT_VAL')
    parser.add_argument('-o', '--output-weights', action='store', dest='output_weights', type=str,
                        help="Location where weights will be saved (as JSON format described below). "
                             "Default: {0}".format(DEFAULT_WEIGHT_OUTPUT_LOCATION),
                        metavar="FULL_PATH", default=DEFAULT_WEIGHT_OUTPUT_LOCATION)
    parser.add_argument('-t', '--pretrained-weights', action='store', dest='pretrained_weights', type=str,
                        help="""
On test mode, this must be provided.
On train mode, this is optional.
Note that you can use the same data set to alter your weights multiple times.
Higher weight means its harder to classify as anomaly, lower weight means its easier
Pre-trained weights JSON file. format:
[
    {
        "feature_indices": [1,2,3], # represents a set of features in indices: 1,2,3
        "weight": <float>
    },
    {
        "feature_indices": [],
        "weight": <float>
    }
]
""")

    args = parser.parse_args()

    # -- validations --
    if len(args.parameters) < 2:
        parser.error('Please specify mode (train or test) and input file')

    mode = args.parameters[0].lower()
    input_file = args.parameters[1]

    assert any(mode == m for m in AVAILABLE_MODES), "Requested mode {0} does not exist. Available modes are: {1}".\
        format(mode, AVAILABLE_MODES)
    assert os.path.isfile(input_file), "Input file: {0} does not exist".format(input_file)
    assert (mode == MODE_TEST and args.pretrained_weights) or mode == MODE_TRAIN, \
        "Test mode must get pre-trained weights as input"
    assert PEARSON_THRESHOLD_MIN <= args.pearson_threshold <= PEARSON_THRESHOLD_MAX, \
        "Requested pearson threshold: {0} should be in range {1}..{2}".format(
            args.pearson_threshold, PEARSON_THRESHOLD_MIN, PEARSON_THRESHOLD_MAX)
    assert args.window_size > 0

    # -- sliding window run --
    for i in range(args.iterations):
        sw = SlidingWindow(mode=mode, learn_rate=args.learn_rate, window_size=args.window_size,
                           pearson_threshold=args.pearson_threshold, debug=args.debug, approximate=args.approximate,
                           feature_weights=FeatureWeights.from_json(args.pretrained_weights, args.output_weights)
                           if mode == "test" or args.pretrained_weights else FeatureWeights(args.output_weights),
                           input_file=input_file)
        start_time = time.time()
        status = sw.run()
        sw.logger.info("Total runtime(iteration {1}): {0:.2f}s".format(time.time() - start_time, i+1))

if __name__ == "__main__":
    main()
