package org.netcomputing.webservices.queues.q4user;

@SuppressWarnings("serial")
public class PacketFormatException extends Exception {
	
	public PacketFormatException(String substring) {
		super("Formatting problem at " + substring);
	}
	
}
