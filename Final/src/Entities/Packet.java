package Entities;

import java.util.Enumeration;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class Packet implements Instance {
	private int protocol;
	private int time;
	private int sourceIp1;
	private int sourceIp2;
	private int sourceIp3;
	private int sourceIp4;
	private int sourcePort;
	private int destinationIp1;
	private int destinationIp2;
	private int destinationIp3;
	private int destinationIp4;
	private int destinationPort;
	private int length;
	private int[] payloadHist;
	private int isAnomaly; // 1 for anomaly 0 for normal

	public Packet(int protocol,int time, int s_ip1, int s_ip2, int s_ip3, int s_ip4, int s_port, int d_ip1, int d_ip2,
			int d_ip3, int d_ip4, int d_port,int length,int[] hist, int isAnomaly) {
		this.protocol = protocol;
		this.time = time;
		this.sourceIp1 = s_ip1;
		this.sourceIp2 = s_ip2;
		this.sourceIp3 = s_ip3;
		this.sourceIp4 = s_ip4;
		this.sourcePort = s_port;
		this.destinationIp1 = d_ip1;
		this.destinationIp2 = d_ip2;
		this.destinationIp3 = d_ip3;
		this.destinationIp4 = d_ip4;
		this.destinationPort = d_port;
		this.length = length; 
		this.payloadHist = hist;
		this.isAnomaly = isAnomaly;
		
	}

	/**
	 * 
	 * @return String of packet attributes without Label Anomaly
	 */
	public String toStringNoLabel() {
		return protocol + ","+ time +"," + sourceIp1 + "," + sourceIp2 + "," + sourceIp3 + "," + sourceIp4 + "," + sourcePort + ","
				+ destinationIp1 + "," + destinationIp2 + "," + destinationIp3 + "," + destinationIp4 + ","
				+ destinationPort + ","+ length +","+ histToString();
	}
	
	@Override
	public String toString() {

		return protocol + ","+ time +"," + sourceIp1 + "," + sourceIp2 + "," + sourceIp3 + "," + sourceIp4 + "," + sourcePort + ","
				+ destinationIp1 + "," + destinationIp2 + "," + destinationIp3 + "," + destinationIp4 + ","
				+ destinationPort + ","+ length +","+ histToString() +","+ isAnomaly;
	}

	private String histToString() {
		
		String result = "";
		int i = 0;
		for(; i < payloadHist.length-1; i++)
		{
				
			result = result+payloadHist[i]+",";
		}
		result = result+payloadHist[i];
	
		return result;
	}

	public int getProtocol() {
		return protocol;
	}

	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public int getSourceIp2() {
		return sourceIp2;
	}

	public void setSourceIp2(int sourceIp2) {
		this.sourceIp2 = sourceIp2;
	}

	public int getSourceIp3() {
		return sourceIp3;
	}

	public void setSourceIp3(int sourceIp3) {
		this.sourceIp3 = sourceIp3;
	}

	public int getSourceIp4() {
		return sourceIp4;
	}

	public void setSourceIp4(int sourceIp4) {
		this.sourceIp4 = sourceIp4;
	}

	public int getDestinationIp2() {
		return destinationIp2;
	}

	public void setDestinationIp2(int destinationIp2) {
		this.destinationIp2 = destinationIp2;
	}

	public int getDestinationIp3() {
		return destinationIp3;
	}

	public void setDestinationIp3(int destinationIp3) {
		this.destinationIp3 = destinationIp3;
	}

	public int getDestinationIp4() {
		return destinationIp4;
	}

	public void setDestinationIp4(int destinationIp4) {
		this.destinationIp4 = destinationIp4;
	}

	public void setSourceIp1(int sourceIp1) {
		this.sourceIp1 = sourceIp1;
	}

	public int getSourceIp1() {
		return sourceIp1;
	}

	public void setSourceIp(int sourceIp1) {
		this.sourceIp1 = sourceIp1;
	}

	public int getSourcePort() {
		return sourcePort;
	}

	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}

	public int getDestinationIp1() {
		return destinationIp1;
	}

	public void setDestinationIp1(int destinationIp1) {
		this.destinationIp1 = destinationIp1;
	}

	public int getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
	}

	public int getIsAnomaly() {
		return isAnomaly;
	}

	public void setIsAnomaly(int isAnomaly) {
		this.isAnomaly = isAnomaly;
	}

	@Override
	public Object copy() {

		return new Packet(this.protocol,this.time, this.sourceIp1, this.sourceIp2, this.sourceIp3, this.sourceIp4,
				this.sourcePort, this.destinationIp1, this.destinationIp2, this.destinationIp3, this.destinationIp4,
				this.destinationPort,this.length,this.payloadHist, this.isAnomaly);
	}

	@Override
	public Attribute attribute(int i) {
		//Attribute result;

		switch (i) {
		case 0:
			//result = new Attribute("Protocol", new LinkedList<String>());
			break;
		case 1:
			break;
		}
		return null;
	}

	@Override
	public Attribute attributeSparse(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute classAttribute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int classIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean classIsMissing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double classValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Instance copy(double[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAttributeAt(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Enumeration<Attribute> enumerateAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equalHeaders(Instance arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String equalHeadersMsg(Instance arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasMissingValue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int index(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void insertAttributeAt(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isMissing(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMissing(Attribute arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMissingSparse(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Instance mergeInstance(Instance arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numAttributes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int numClasses() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int numValues() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void replaceMissingValues(double[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClassMissing() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClassValue(double arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClassValue(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMissing(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMissing(Attribute arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(int arg0, double arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(Attribute arg0, double arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(Attribute arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueSparse(int arg0, double arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWeight(double arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String stringValue(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stringValue(Attribute arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] toDoubleArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString(Attribute arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString(Attribute arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toStringMaxDecimalDigits(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toStringNoWeight() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toStringNoWeight(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double value(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double value(Attribute arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double valueSparse(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double weight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Instances dataset() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instances relationalValue(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instances relationalValue(Attribute arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDataset(Instances arg0) {
		// TODO Auto-generated method stub

	}

	

}
