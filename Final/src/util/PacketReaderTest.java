package util;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;

import infra.AbstractPacketReader;
import infra.AbstractPacketWriter;

public class PacketReaderTest {

	private static final String FILE = "c:\\Desktop\file.txt";

	@Test
	public void test() {
		
		AbstractPacketReader r;
	
		int numPacketsRead =0;
		try {
			r = new WireSharkPacketReader(new BufferedReader(new FileReader(FILE)));
			 numPacketsRead = r.readPackets();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(5, numPacketsRead);
		fail("Not yet implemented");
	}

}
