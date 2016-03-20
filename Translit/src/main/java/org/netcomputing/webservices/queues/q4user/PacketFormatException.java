package org.netcomputing.webservices.queues.q4user;

/**
 * 
 * @author Stijn
 * 
 * When a TranslationPacket is not formatted as expected,
 * this exception contains the tokens that caused the conflict
 */

@SuppressWarnings("serial")
public class PacketFormatException extends Exception {
	
	public PacketFormatException(String substring) {
		super("Formatting problem at " + substring);
	}
	
}
