package tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import Entities.Packet;
import util.WekaPacketWriter;

public class WriteTest {

	public static void main(String[] args) {
		
		List<Packet> packets = new LinkedList<Packet>();
		int[] hist = new int[256];
		hist[1]++;
		hist[2]++;
		packets.add(new Packet("Http".hashCode(), 1400000, 198, 220, 1, 10, 5400, 192, 0, 0, 1, 80,1000,hist, 1));
		packets.add(new Packet("TCP".hashCode(), 1400001, 192, 0, 0, 1, 8080, 242, 1, 1, 20, 80,1500,hist, 0));
		packets.add(new Packet("UDP".hashCode(), 1400000, 198, 220, 1, 10, 5400, 192, 0, 0, 1, 80,1300,hist, 1));
		packets.add(new Packet("Http".hashCode(), 1400001, 192, 0, 0, 1, 8080, 242, 1, 1, 20, 80,1600,hist, 0));
		
		try {
			WekaPacketWriter writer = 
					new WekaPacketWriter("Writer Test", 
						new BufferedWriter(
							new FileWriter("assets/writeToMe.arff")));
			
			writer.writePackets(packets);
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
