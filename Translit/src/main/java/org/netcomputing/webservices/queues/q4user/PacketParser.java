package org.netcomputing.webservices.queues.q4user;

public class PacketParser {
	
	private int pdx = 0;
	private String packet;
	
	public PacketParser(String pkt) {
		packet = pkt;
	}
	
	public void accept(String str) throws PacketFormatException {
		for(int idx = 0; idx != str.length(); ++idx) {
			if(str.charAt(idx) != packet.charAt(pdx))
				throw new PacketFormatException(packet.substring(pdx));
			++pdx;
		}
	}
	
	public String accept(int count) throws PacketFormatException {
		StringBuilder irep = new StringBuilder();
		for(int idx = 0; idx != count; ++idx) {
			irep.append(packet.charAt(pdx));
			++pdx;
		}
		return irep.toString();
	}
	
	public int nextInt() {
		StringBuilder irep = new StringBuilder();
		for(; packet.charAt(pdx) != ')'; ++pdx)
			irep.append(packet.charAt(pdx));
		return Integer.parseInt(irep.toString());
	}
	
}