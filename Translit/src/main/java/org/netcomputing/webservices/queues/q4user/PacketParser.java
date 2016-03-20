package org.netcomputing.webservices.queues.q4user;

/**
 * 
 * @author Stijn
 * 
 * General parser made specifically for translation packets, but
 * could be made reasonably useful for parsing other things.
 */
public class PacketParser {
	
	/**
	 * Current index in the packet string.
	 */
	private int pdx = 0;
	/**
	 * String currently being parsed
	 */
	private String packet;
	
	/**
	 * Create a new PacketParser parsing pkt
	 * @param pkt
	 * The string (packet) being parsed
	 */
	public PacketParser(String pkt) {
		packet = pkt;
	}
	
	/**
	 * Tries to see if the prefix of packet matches str
	 * @param str
	 * Expected prefix (verbatim)
	 * @throws PacketFormatException
	 * If str is not matched exactly
	 */
	public void accept(String str) throws PacketFormatException {
		for(int idx = 0; idx != str.length(); ++idx) {
			if(str.charAt(idx) != packet.charAt(pdx))
				throw new PacketFormatException(packet.substring(pdx));
			++pdx;
		}
	}
	
	/**
	 * Accepts a number of characters and returns them as a String
	 * @param count
	 * The number of characters to accept
	 * @return
	 * That number of prefix characters concatenated
	 * @throws PacketFormatException
	 */
	public String accept(int count) throws PacketFormatException {
		StringBuilder irep = new StringBuilder();
		for(int idx = 0; idx != count; ++idx) {
			irep.append(packet.charAt(pdx));
			++pdx;
		}
		return irep.toString();
	}
	
	/**
	 * Accepts a number of characters until the next closing parenthesis,
	 * then parses them as an integer.
	 * @return
	 * The integer representation of that number
	 */
	public int nextInt() {
		StringBuilder irep = new StringBuilder();
		for(; Character.isDigit(packet.charAt(pdx)); ++pdx)
			irep.append(packet.charAt(pdx));
		return Integer.parseInt(irep.toString());
	}
	
}