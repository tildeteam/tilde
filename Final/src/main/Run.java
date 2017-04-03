package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;

import Entities.Packet;
import Entities.PacketFields;
import util.WekaPacketWriter;

public class Run {

	
	public static void main(String[] args) {
		
		try {
			
			Packet[] packets = {new Packet("http", InetAddress.getLocalHost(), 8080, InetAddress.getByName("google.com"), 8080, 0),
								new Packet("http", InetAddress.getLocalHost(), 8080, InetAddress.getByName("google.com"), 8080, 0),
								new Packet("http", InetAddress.getLocalHost(), 8080, InetAddress.getByName("google.com"), 8080, 1),
								new Packet("http", InetAddress.getLocalHost(), 8080, InetAddress.getByName("google.com"), 8080, 0),
								new Packet("http", InetAddress.getLocalHost(), 8080, InetAddress.getByName("google.com"), 8080, 0)};
			
			//WireSharkPacketReader reader = new WireSharkPacketReader(new BufferedReader(new FileReader("assets/readFromMe.txt")));
			
			WekaPacketWriter writer = new WekaPacketWriter("TestProject",new BufferedWriter(new FileWriter("assets/writeToMe.txt")), 
					PacketFields.PROTOCOL,
					PacketFields.SOURCE_IP,
					PacketFields.SOURCE_PORT,
					PacketFields.DESTINATION_IP,
					PacketFields.DESTINATION_PORT,
					PacketFields.IS_ANOMALY);
			
			
			writer.writePackets(packets);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
