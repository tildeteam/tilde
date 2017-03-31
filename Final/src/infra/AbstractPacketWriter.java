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
	public void write(char[] arg0, int arg1, int arg2) throws IOException {
		writer.write(arg0, arg1, arg2);
	}

	public abstract void writePackets();

}
