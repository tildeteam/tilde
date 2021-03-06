package util;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import models.Packet;
import models.PacketFields;

public class WindowedWekaPacketWriter extends WekaPacketWriter {

	private List<Packet> window;
	private int windowSize;
	private int windowOffset;

	public WindowedWekaPacketWriter(String projectName, Writer writer, int windowSize) throws IOException {
		super(projectName, writer);

		this.windowSize = windowSize;
		windowOffset = 0;
		window = new LinkedList<Packet>();
	}

	public int getWindowSize() {
		return windowSize;
	}

	public List<Packet> getWindow() {
		return window;
	}

	protected void writeWindow() {

		Packet[] array = windowToArray();

		try {

			this.writeInstances(array);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private Packet[] windowToArray() {
		Packet[] p = new Packet[getWindowSize()];

		for (int i = 0; i < getWindowSize(); i++) {
			p[i] = getWindow().get(i);
		}

		return p;
	}

	/**
	 * overriding the super class method to write a Window's attributes. Marking
	 * attributeA<i> for the i's packet in the window
	 * 
	 * @author guy
	 * 
	 */
	@Override
	protected void writeAttributes() {
		ArrayList<PacketFields> array = getPacketFieldsList();
		for (int i = 0; i < getWindowSize(); i++) {
			for (PacketFields field : array) {

				if (getWindowSize() == 1) // single packet window we should
											// include isAnomaly field
				{
					writeAttribute(field.name() + "<" + (i + 1) + ">", field.getType());
				} else { // we shouldnt write individual isAnomaly fields (only
							// the final window label)
					if (!field.name().equals("IS_ANOMALY")) {
						writeAttribute(field.name() + "<" + (i + 1) + ">", field.getType());
					}
				}
			}
		}
	}

	protected void iterateWindow() {

		getWindow().add(this.getData().get(windowOffset + windowSize)); // get
																		// the
																		// next
																		// Data
																		// instance
		getWindow().remove(0); // remove the oldest data instance from the
								// window (window movement)
		windowOffset++;
	}

	public int getWindowOffset() {
		return windowOffset;
	}

	@Override
	protected void writeData() throws IOException {
		write("\n");
		write("@DATA\n");

		initWindow(); // initiallizing the first n packets in the window

		while (windowSize + windowOffset <= this.getData().size()) {
			System.out.println("writing window: " + (windowOffset + 1));
			writeWindow();

			if (windowOffset + windowSize != this.getData().size()) {
				iterateWindow();
			} else {
				windowOffset = this.getData().size();
			}

		}

	}

	private void initWindow() {
		for (int i = 0; i < getWindowSize(); i++) {
			getWindow().add(getData().get(i));
		}

	}

}
