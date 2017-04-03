package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import Entities.Packet;
import infra.AbstractPacketReader;

public class WireSharkPacketReader extends AbstractPacketReader {

	public WireSharkPacketReader(BufferedReader reader) {
		super(reader);
	}

	@Override
	public int readPackets(List<Packet> packets) {
		String line;
		int packetCounter = 0 ;
		try {
			while((line = getReader().readLine()) != null)
			{
				if(packetCounter != 0)//if not headers
				{
					String[] lineSeperated = line.replaceAll("\"", "").split(",");
					packets.add(new Packet(lineSeperated[0], 
										   lineSeperated[1], 
										   Integer.parseInt(lineSeperated[2]), 
										   lineSeperated[3], 
										   Integer.parseInt(lineSeperated[4]), 
										   Integer.parseInt(lineSeperated[5])));
					
				}
				packetCounter++;
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return packetCounter-1;
	}

	
}
