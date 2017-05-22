package util;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import Entities.Packet;
import Entities.PacketFields;
import infra.AbstractPacketWriter;

public class WekaPacketWriter extends AbstractPacketWriter {

	private String projectName;
	private boolean isAttributesInitiallized = false;
	private ArrayList<PacketFields> packetFieldsList = new ArrayList<PacketFields>();
	private List<Packet> data;

	public WekaPacketWriter(String projectName, Writer writer) {
		super(writer);
		this.projectName = projectName;
		for (PacketFields field : PacketFields.values()) {
			packetFieldsList.add(field);
		}
	}

	public ArrayList<PacketFields> getPacketFieldsList() {
		return packetFieldsList;
	}

	@Override
	public void writePackets(List<Packet> packets) {
		setData(packets);
		
		try {
			writeProject();
			writeAttributes();
			isAttributesInitiallized = true;
			writeData();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	protected void setData(List<Packet> packets) {
		this.data = packets;
		
	}

	protected void writeData() throws IOException {
		write("\n");
		write("@DATA\n");
		for (Packet packet : data) {
			writeInstances(packet);
		}
	}

	protected void writeProject() throws IOException {
		write("@RELATION " + projectName + "\n\n");

	}

	protected void writeAttribute(String name, String type) {
		try {

			write("@ATTRIBUTE " + name + " " + type + "\n");
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

	/**
	 * this function writes all packets received in p as one Instance in the
	 * output file
	 * 
	 * @param p
	 * @throws IOException
	 */
	public void writeInstances(Packet... p) throws IOException {
		if (isAttributesInitiallized == false) {
			throw new IOException("cannont write data until file headers are written");
		}

		for (int i = 0; i < p.length; i++) {

			if (i != p.length - 1) {
				System.out.print(p[i].toString() + ",");
				write(p[i].toString() + ",");
			} else {
				System.out.println(p[i].toString());
				write(p[i].toString());
			}
		}
		write("\n");

	}

	protected void writeAttributes() {
		for (PacketFields f : packetFieldsList) {
			writeAttribute(f.name(), f.getType());
		}

	}

	public void setAttributesInitiallized(boolean isAttributesInitiallized) {
		this.isAttributesInitiallized = isAttributesInitiallized;
	}

	public boolean isAttributesInitiallized() {
		return isAttributesInitiallized;
	}

	public String getProjectName() {
		return projectName;
	}

	public List<Packet> getData() {
		return data;
	}

}
