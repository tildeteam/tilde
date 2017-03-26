package util;

import infra.AbstractDumpFileConverter;

public class DumpFileConverterTest {

	public static void main(String[] args) {
		String dumpFilesDirectoryPath = null;
		AbstractDumpFileConverter c = new NormalDumpFileConverter(dumpFilesDirectoryPath);
		String vectorFile = c.convertDumpToVectors();
	}

}
