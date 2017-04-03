package Entities;

import java.net.InetAddress;
import java.util.Enumeration;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;


public class Packet implements Instance {
	private String protocol;
	private InetAddress sourceIp;
	private int sourcePort;
	private InetAddress destinationIp;
	private int destinationPort;
	private int isAnomaly;	//1 for anomaly 0 for normal
	
	
	public Packet(String protocol,InetAddress s_ip ,int s_port, InetAddress d_ip, int d_port,int isAnonaly) {
		this.protocol = protocol;
		this.sourceIp = s_ip;
		this.sourcePort = s_port;
		this.destinationIp = d_ip;
		this.destinationPort = d_port;
		this.isAnomaly = isAnonaly;
		
	}

	
	@Override
	public String toString() {
		
		return protocol+","+sourceIp+","+sourcePort+","+destinationIp+","+destinationPort+","+isAnomaly;
	}
	
	public String getProtocol() {
		return protocol;
	}



	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}



	public InetAddress getSourceIp() {
		return sourceIp;
	}



	public void setSourceIp(InetAddress sourceIp) {
		this.sourceIp = sourceIp;
	}



	public int getSourcePort() {
		return sourcePort;
	}



	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}



	public InetAddress getDestinationIp() {
		return destinationIp;
	}



	public void setDestinationIp(InetAddress destinationIp) {
		this.destinationIp = destinationIp;
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
		
		return new Packet(this.protocol,this.sourceIp,this.sourcePort,this.destinationIp,this.destinationPort,this.isAnomaly);
	}

	@Override
	public Attribute attribute(int arg0) {
		// TODO Auto-generated method stub
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
