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
					String protocol = seperatedLine[0];
					int timeSeconds = Integer.parseUnsignedInt(seperatedLine[1]);
					//int timeMilli = Integer.parseInt(seperatedLine[2]);
					int s_ip1= Integer.parseInt(seperatedLine[3]);
					int s_ip2 = Integer.parseInt(seperatedLine[4]);
					int s_ip3 = Integer.parseInt(seperatedLine[5]);
					int s_ip4 = Integer.parseInt(seperatedLine[6]);
					int s_port = Integer.parseInt(seperatedLine[7]);
					int d_ip1 = Integer.parseInt(seperatedLine[8]);
					int d_ip2 = Integer.parseInt(seperatedLine[9]);
					int d_ip3 = Integer.parseInt(seperatedLine[10]);
					int d_ip4 = Integer.parseInt(seperatedLine[11]);
					int d_port = Integer.parseInt(seperatedLine[12]);
					int isAnomaly = Integer.parseInt(seperatedLine[13]);
					
					Packet p = new Packet(protocol, timeSeconds, s_ip1, s_ip2, s_ip3, s_ip4, s_port, d_ip1, d_ip2, d_ip3, d_ip4, d_port, isAnomaly);
					System.out.println(p.toString());
					packets.add(p);
					
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
