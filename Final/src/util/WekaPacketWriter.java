package util;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import Entities.Packet;
import Entities.PacketFields;
import infra.AbstractPacketWriter;

public class WekaPacketWriter extends AbstractPacketWriter {

	private String projectName;
	private boolean isAttributesInitiallized = false;
	private ArrayList<PacketFields> packetFieldsList = new ArrayList<PacketFields>();

	public WekaPacketWriter(String projectName, Writer writer, PacketFields... fields) {
		super(writer);
		this.projectName = projectName;
		for (PacketFields packetFields : fields) {
			packetFieldsList.add(packetFields);
		}
	}

	@Override
	public void writePackets(Packet[] packets) {
		try {
			write("@RELATION " + projectName + "\n\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (PacketFields f : packetFieldsList) {
			writeAttribute(f.name(), f.getType());
		}
		try {
			write("\n");
			write("@DATA\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		isAttributesInitiallized = true;

		for (Packet packet : packets) {
			try {
				writeInstance(packet);
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	private void writeAttribute(String name , String type) {
		try {

			write("@ATTRIBUTE " + name +" "+type+"\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void writeInstance(Packet p) throws IOException {
		if (isAttributesInitiallized == false) {
			throw new IOException("cannont write data until file headers are written");
		}
		write(p.toString() + "\n");
	}

}
