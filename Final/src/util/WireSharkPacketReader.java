package util;
import java.io.Reader;

import infra.AbstractPacketReader;

public class WireSharkPacketReader extends AbstractPacketReader {

	public WireSharkPacketReader(Reader reader) {
		super(reader);
	}

	@Override
	public int readPackets() {
		
		return 0;
	}

	

}
