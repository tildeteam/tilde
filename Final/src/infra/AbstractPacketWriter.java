package infra;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import Entities.Packet;

public abstract class AbstractPacketWriter extends Writer {

	private Writer writer;

	public AbstractPacketWriter(Writer writer) {
		this.writer = writer;
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	
	public abstract void writePackets(List<Packet> p);
	
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException
	{
		writer.write(cbuf, off, len);
	}

}
