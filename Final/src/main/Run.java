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
			List<Packet> packetsToWrite = Arrays.asList(
								new Packet("http", InetAddress.getLocalHost(), 8080, InetAddress.getByName("google.com"), 8080, 0),
								new Packet("http", InetAddress.getLocalHost(), 8080, InetAddress.getByName("google.com"), 8080, 0),
								new Packet("http", InetAddress.getLocalHost(), 8080, InetAddress.getByName("google.com"), 8080, 1),
								new Packet("http", InetAddress.getLocalHost(), 8080, InetAddress.getByName("google.com"), 8080, 0),
								new Packet("http", InetAddress.getLocalHost(), 8080, InetAddress.getByName("google.com"), 8080, 0)
								);
			
			WireSharkPacketReader reader = new WireSharkPacketReader(new BufferedReader(new FileReader("assets/readFromMe.txt")));
			numOfPacketsRead = reader.readPackets(packetsRead);
			System.out.println(numOfPacketsRead);
			
			
			WekaPacketWriter writer = new WekaPacketWriter("TestProject",new BufferedWriter(new FileWriter("assets/writeToMe.txt")), 
					PacketFields.PROTOCOL,
					PacketFields.SOURCE_IP,
					PacketFields.SOURCE_PORT,
					PacketFields.DESTINATION_IP,
					PacketFields.DESTINATION_PORT,
					PacketFields.IS_ANOMALY);
			
			
			writer.writePackets(packetsToWrite);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
