package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Entities.Packet;
import Entities.PacketFields;
import util.WekaPacketWriter;
import util.WireSharkPacketReader;

public class Run {

	
	public static void main(String[] args) {
		
		try {
			int numOfPacketsRead;
			List<Packet> packetsRead = new ArrayList<Packet>();
			
			WireSharkPacketReader reader = new WireSharkPacketReader(new BufferedReader(new FileReader("assets/readFromMe.txt")));
			numOfPacketsRead = reader.readPackets(packetsRead);
			System.out.println(numOfPacketsRead);
			
			
			
			List<Packet> packetsToWrite = Arrays.asList(
					new Packet("http", "127.0.0.1", 8080, "194.0.0.4", 8080, 0),
					new Packet("http", "127.0.0.1", 8080, "194.0.0.4", 8080, 0),
					new Packet("http", "127.0.0.1", 8080, "194.0.0.4", 8080, 1),
					new Packet("http", "127.0.0.1", 8080, "194.0.0.4", 8080, 0),
					new Packet("http", "127.0.0.1", 8080, "194.0.0.4", 8080, 0)
					);
			WekaPacketWriter writer = new WekaPacketWriter("TestProject",new BufferedWriter(new FileWriter("assets/writeToMe.arff")), 
					PacketFields.PROTOCOL,
					PacketFields.SOURCE_IP,
					PacketFields.SOURCE_PORT,
					PacketFields.DESTINATION_IP,
					PacketFields.DESTINATION_PORT,
					PacketFields.IS_ANOMALY);
			
			
			writer.writePackets(packetsRead);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
