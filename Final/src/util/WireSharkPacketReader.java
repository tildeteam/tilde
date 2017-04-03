package util;
import java.io.Reader;
import java.util.List;

import Entities.Packet;
import infra.AbstractPacketReader;

public class WireSharkPacketReader extends AbstractPacketReader {

	public WireSharkPacketReader(Reader reader) {
		super(reader);
	}

	@Override
	public int readPackets(List<Packet> packets) {
		
		return 0;
	}

	

}
