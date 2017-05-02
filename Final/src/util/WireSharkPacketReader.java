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
					String[] seperatedLine = line.replaceAll("\"", "").replaceAll("\\.", ",").split(",");
					
					packets.add(new Packet((seperatedLine[0]), 
										   Integer.parseInt(seperatedLine[1]), 
										   Integer.parseInt(seperatedLine[2]), 
										   Integer.parseInt(seperatedLine[3]), 
										   Integer.parseInt(seperatedLine[4]), 
										   Integer.parseInt(seperatedLine[5]),
										   Integer.parseInt(seperatedLine[6]),
										   Integer.parseInt(seperatedLine[7]),
										   Integer.parseInt(seperatedLine[8]),
										   Integer.parseInt(seperatedLine[9]),
										   Integer.parseInt(seperatedLine[10]),
										   Integer.parseInt(seperatedLine[11])));
					
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
