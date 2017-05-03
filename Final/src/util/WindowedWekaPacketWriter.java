package util;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Entities.Packet;
import Entities.PacketFields;

public class WindowedWekaPacketWriter extends WekaPacketWriter {

	private List<Packet> window = new LinkedList<Packet>();
	private int windowSize;
	private int windowOffset;

	public WindowedWekaPacketWriter(String projectName, Writer writer, int windowSize,
			PacketFields... fields) throws IOException {
		super(projectName, writer, fields);

		this.windowSize = windowSize;
		windowOffset = 0;
	}

	public int getWindowSize() {
		return windowSize;
	}

	public List<Packet> getWindow() {
		return window;
	}

	protected void writeWindow() {

		Packet[] array =  windowToArray();
		
		try {
			this.writeInstances(array);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	

	private Packet[] windowToArray() {
		Packet[] p = new Packet[getData().size()];

		for(int i = 0 ; i < getData().size(); i++)
		{
			p[i] = getData().get(i);
		}
		
		return p;
	}

	@Override
	protected void writeAttributes() {
		ArrayList<PacketFields> array = getPacketFieldsList();
		for (int i = 0 ; i < getWindowSize() ; i++) {
			for (PacketFields field : array) {
				writeAttribute(field.name()+"<"+(i+1)+">", field.getType());
			}
		}
	}

	protected void iterateWindow() {
		
		getWindow().add(this.getData().get(windowOffset + windowSize)); //get the next Data instance 
		getWindow().remove(0); //remove the oldest data instance from the window (window movement)
		windowOffset++;
	}

	
	public int getWindowOffset() {
		return windowOffset;
	}
	
	
	@Override
	protected void writeData() throws IOException {
		write("\n");
		write("@DATA\n");
		
		while(getWindowSize() + getWindowOffset() < this.getData().size()){
			
			writeWindow();
			iterateWindow();
			
		}
		
		
	}
	
	
	
	
	
	
	
	
}