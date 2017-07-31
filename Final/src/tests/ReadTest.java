package tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Packet;
import util.WireSharkPacketReader;

public class ReadTest {

	public static void main(String[] args) {
		
		List<Packet> packetsRead = new ArrayList<Packet>();
		
		try {
			WireSharkPacketReader reader = new WireSharkPacketReader(
					new BufferedReader(
					new FileReader("assets/readFromMe")));
			
			int numOfPacketsRead = reader.readPackets(packetsRead);
			System.out.println(numOfPacketsRead+" packets read");
			reader.close();
			
			System.out.println(packetsRead);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
