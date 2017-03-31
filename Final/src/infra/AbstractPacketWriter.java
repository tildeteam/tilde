package infra;

import java.io.IOException;
import java.io.Writer;

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

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public abstract void writePackets();

}
