package tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Packet;
import util.WindowedWekaPacketWriter;
import util.WireSharkPacketReader;

public class ReadAndWriteTest {

	public static void main(String[] args) {

		try {
			int numOfPacketsRead=0;
			List<Packet> packetsRead = new ArrayList<Packet>();

			WireSharkPacketReader reader = new WireSharkPacketReader(
					new BufferedReader(new FileReader("assets/dataLabeled8000p")));
			numOfPacketsRead = reader.readPackets(packetsRead);
			System.out.println("# of packets read: "+numOfPacketsRead);

			WindowedWekaPacketWriter writer = new WindowedWekaPacketWriter("w1_8000p",
					new BufferedWriter(new FileWriter("assets/windowSize1_8000p.arff")), 1);
			

			writer.writePackets(packetsRead);
			writer.flush();
			writer.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
