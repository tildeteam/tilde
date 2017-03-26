package infra;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public abstract class AbstractDumpFileConverter{
	private String dumpDirectoryPath;
	private BufferedReader reader;
	
	public AbstractDumpFileConverter(String dumpDirectoryPath) {
		this.dumpDirectoryPath = dumpDirectoryPath;
		try {
			reader = new BufferedReader(new FileReader(dumpDirectoryPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public abstract String convertDumpToVectors();
	
	public void setDumpDirectoryPath(String dumpDirectoryPath) {
		this.dumpDirectoryPath = dumpDirectoryPath;
	}
	
	public String getDumpDirectoryPath() {
		return dumpDirectoryPath;
	}
	
}
