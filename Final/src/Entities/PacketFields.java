package Entities;


public enum PacketFields {
	PROTOCOL("{HTTP,TCP,UDP,ICMP}"),
	TIME("NUMERIC"),
	SOURCE_IP1("NUMERIC"),
	SOURCE_IP2("NUMERIC"),
	SOURCE_IP3("NUMERIC"),
	SOURCE_IP4("NUMERIC"),
	SOURCE_PORT("NUMERIC"),
	DESTINATION_IP1("NUMERIC"),
	DESTINATION_IP2("NUMERIC"),
	DESTINATION_IP3("NUMERIC"),
	DESTINATION_IP4("NUMERIC"),
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
