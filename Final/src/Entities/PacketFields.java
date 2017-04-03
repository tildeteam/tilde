package Entities;

public enum PacketFields {
	PROTOCOL("string"),
	SOURCE_IP("NOMINAL"),
	SOURCE_PORT("NUMERIC"),
	DESTINATION_IP("NOMINAL"),
	DESTINATION_PORT("NUMERIC"),
	IS_ANOMALY("{0,1}");
	
	private String type;
	
	private PacketFields(String s){
		this.type = s;
	}
	
	public String getType(){
		return this.type;
	}
}
