package infra;

import java.io.IOException;
import java.io.Reader;

public abstract class AbstractPacketReader extends Reader{
	
	private Reader reader;
	
	public AbstractPacketReader(Reader reader) {
		this.reader = reader;
		
	}
	
	/**
	 * read Packets from a file and return the number of packets read
	 * @return
	 */
	public abstract int readPackets();

	@Override
	public void close() throws IOException {
		reader.close();
		
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return reader.read(cbuf, off, len);
		
	}
	
	
}
