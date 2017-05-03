package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Entities.Packet;
import util.WireSharkPacketReader;

public class ReadTest {

	public static void main(String[] args) {
		
		List<Packet> packetsRead = new ArrayList<Packet>();
		
		try {
			WireSharkPacketReader reader = new WireSharkPacketReader(
					new BufferedReader(
					new FileReader("assets/readFromMe.txt")));
			
			reader.readPackets(packetsRead);
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
