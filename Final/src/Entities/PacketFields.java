package Entities;

public enum PacketFields {
	PROTOCOL("{HTTP,TCP,UDP,ICMP}"),
	SOURCE_IP("string"),
	SOURCE_PORT("NUMERIC"),
	DESTINATION_IP("string"),
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
