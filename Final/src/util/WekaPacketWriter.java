package util;

import java.io.IOException;
import java.io.Writer;

import Entities.Packet;
import infra.AbstractPacketWriter;

public class WekaPacketWriter extends AbstractPacketWriter {

	private boolean isHeadersInitiallized = false;

	public WekaPacketWriter(Writer writer) {
		super(writer);

	}

	@Override
	public void writePackets() {
		if (isHeadersInitiallized) {

		}

	}

	private void writeAttribute(String name){
		try {
			write("@ATTRIBUTE "+name);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	private void writeInstance(Packet p) throws IOException{
		if(isHeadersInitiallized == false){
			throw new IOException("cannont write data until file headers are written");
		}
		write(""+p.getProtocol()+","+p.getDestinationIp()+","+p.getDestinationPort()+","+p.getSourceIp()+
					","+p.getSourcePort()+","+p.getIsAnomaly());
	}
}
