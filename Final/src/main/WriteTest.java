package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import Entities.Packet;
import Entities.PacketFields;
import util.WekaPacketWriter;

public class WriteTest {

	public static void main(String[] args) {
		
		List<Packet> packets = new LinkedList<Packet>();
		packets.add(new Packet("Http", 1400000, 198, 220, 1, 10, 5400, 192, 0, 0, 1, 80, 1));
		packets.add(new Packet("TCP", 1400001, 192, 0, 0, 1, 8080, 242, 1, 1, 20, 80, 0));
		packets.add(new Packet("UDP", 1400000, 198, 220, 1, 10, 5400, 192, 0, 0, 1, 80, 1));
		packets.add(new Packet("Http", 1400001, 192, 0, 0, 1, 8080, 242, 1, 1, 20, 80, 0));
		
		try {
			WekaPacketWriter writer = 
					new WekaPacketWriter("Writer Test", 
						new BufferedWriter(
							new FileWriter("assets/writeToMe.arff")), 
						PacketFields.PROTOCOL,
						PacketFields.TIME,
						PacketFields.SOURCE_IP1, 
						PacketFields.SOURCE_IP2, 
						PacketFields.SOURCE_IP3, 
						PacketFields.SOURCE_IP4,
						PacketFields.SOURCE_PORT, 
						PacketFields.DESTINATION_IP1, 
						PacketFields.DESTINATION_IP2,
						PacketFields.DESTINATION_IP3, 
						PacketFields.DESTINATION_IP4, 
						PacketFields.DESTINATION_PORT,
						PacketFields.IS_ANOMALY);
			
			writer.writePackets(packets);
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
