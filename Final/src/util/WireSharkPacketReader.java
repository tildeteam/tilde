package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Entities.Packet;
import infra.AbstractPacketReader;

public class WireSharkPacketReader extends AbstractPacketReader {

	public WireSharkPacketReader(BufferedReader reader) {
		super(reader);
	}

	@Override
	public int readPackets(List<Packet> packets) {
		String line;
		int packetCounter = 0;
		try {
			while ((line = getReader().readLine()) != null) {
				if (packetCounter != 0)// if not headers
				{
					// "TCP","1493804267.060354329","10.10.249.7","62339","10.10.248.20","80","60","62339
					// > 80 [ACK] Seq=1 Ack=1 Win=256 Len=1"
					String[] seperatedLine = line.substring(1, line.length() - 1).split("\"\\,\"");
					// TCP,1493804267.060354329,10.10.249.7,62339,10.10.248.20,80,60,62339
					// > 80 [ACK] Seq=1 Ack=1 Win=256 Len=1
//					for (String string : seperatedLine) {
//						System.out.print(string + ",");
//					}
//					System.out.println("");
					int protocol = seperatedLine[0].hashCode();

					String[] time = seperatedLine[1].split("\\.");
					
					int timeSeconds = Integer.parseUnsignedInt(time[0]);
					// int timeMilli = Integer.parseInt(seperatedLine[2]);

					int s_ip1, s_ip2, s_ip3, s_ip4;
					try {
						
						
						
						String[] sourceIp = seperatedLine[2].split("\\.");

						s_ip1 = Integer.parseInt(sourceIp[0]);
						s_ip2 = Integer.parseInt(sourceIp[1]);
						s_ip3 = Integer.parseInt(sourceIp[2]);
						s_ip4 = Integer.parseInt(sourceIp[3]);

					} catch (NumberFormatException e) {

						s_ip1 = seperatedLine[2].hashCode();
						s_ip2 = 0;
						s_ip3 = 0;
						s_ip4 = 0;
					}

					int s_port;
					try{
						s_port = Integer.parseInt(seperatedLine[3]);
					}
					catch(NumberFormatException e){
						s_port = 0;
					}
					
					
					int d_ip1, d_ip2, d_ip3, d_ip4;
					try {
						String[] destinationIp = seperatedLine[4].split("\\.");

						d_ip1 = Integer.parseInt(destinationIp[0]);
						d_ip2 = Integer.parseInt(destinationIp[1]);
						d_ip3 = Integer.parseInt(destinationIp[2]);
						d_ip4 = Integer.parseInt(destinationIp[3]);
					} catch (NumberFormatException e) {

						d_ip1 = seperatedLine[4].hashCode();
						d_ip2 = 0;
						d_ip3 = 0;
						d_ip4 = 0;
					}


					int d_port;
					try{
						d_port = Integer.parseInt(seperatedLine[5]);
					}
					catch(NumberFormatException e){
						d_port = 0;
					}
					
					int length = Integer.parseInt(seperatedLine[6]);

					int[] hist = new int[256];
					
					String payload;
					try{
						payload = seperatedLine[7];
					}catch(ArrayIndexOutOfBoundsException e){
						payload = "";
					}
					
					
					

					
					
					for (int i = 0; i < payload.length(); i++) {
						char c = payload.charAt(i);
						hist[(int) c]++;
					}

					// int isAnomaly = Integer.parseInt(seperatedLine[13]);
					int isAnomaly = new Random().nextInt(2);
					Packet p = new Packet(protocol, timeSeconds, s_ip1, s_ip2, s_ip3, s_ip4, s_port, d_ip1, d_ip2,
							d_ip3, d_ip4, d_port, length, hist, isAnomaly);
				//	System.out.println(p.toString());
					
					packets.add(p);

				}
				packetCounter++;
				System.out.println(packetCounter-1);
			}
		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return packetCounter - 1;
	}

}
