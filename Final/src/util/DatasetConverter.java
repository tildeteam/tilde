package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import infra.AbstractPacketReader;
import infra.AbstractPacketWriter;
import models.Packet;

public class DatasetConverter {

	private AbstractPacketReader reader;
	private AbstractPacketWriter writer;
	
	private String INPUT_FILE;
	private String OUTPUT_FILE;
	private int windowSize = 1;
	
	
	public DatasetConverter(String inputFilePath, String outputFilePath, int windowSize) {
		INPUT_FILE = inputFilePath;
		OUTPUT_FILE = outputFilePath;
		this.windowSize = windowSize;
	}
	public DatasetConverter(String inputFilePath,String outputFilePath) {
		INPUT_FILE = inputFilePath;
		OUTPUT_FILE = outputFilePath;
		this.windowSize = 1;
	}
	
	public void convert(){
		try {
			Scanner scanner = new Scanner(System.in);

			System.out.println("enter the project's name: ");
			String projectName = scanner.nextLine();
			int numOfPacketsRead=0;
			List<Packet> packetsRead = new ArrayList<Packet>();

			reader = new WireSharkPacketReader(
					new BufferedReader(new FileReader(INPUT_FILE)));
			numOfPacketsRead = reader.readPackets(packetsRead);
			System.out.println("# of packets read: "+numOfPacketsRead);

			writer = new WindowedWekaPacketWriter(projectName,
					new BufferedWriter(new FileWriter(OUTPUT_FILE)), windowSize);
			

			writer.writePackets(packetsRead);
			writer.flush();
			writer.close();
			reader.close();
			scanner.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
