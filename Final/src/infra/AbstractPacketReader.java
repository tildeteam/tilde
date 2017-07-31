package infra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import models.Packet;

public abstract class AbstractPacketReader extends Reader{
	
	private BufferedReader reader;
	
	public AbstractPacketReader( BufferedReader reader) {
		this.reader = reader;
		
	}
	
	/**
	 * read Packets from a file and return the number of packets read
	 * @return
	 */
	public abstract int readPackets(List<Packet> packets);

	@Override
	public void close() throws IOException {
		reader.close();
		
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return reader.read(cbuf, off, len);
		
	}
	
	
	
	public BufferedReader getReader() {
		return reader;
	}
	
	
	
}
