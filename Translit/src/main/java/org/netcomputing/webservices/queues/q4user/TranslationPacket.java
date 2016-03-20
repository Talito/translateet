package org.netcomputing.webservices.queues.q4user;

/**
 * 
 * @author Stijn and Jose
 * Container for translation requests
 */
public class TranslationPacket {

	private String lang;
	private String msg;
	protected String packet;
	protected PacketParser pp;
	
	protected static final String CLOSE = ")=";
	protected static final String LOPEN = "Language(l=";
	protected static final String TOPEN = "Message(l=";
	
	/**
	 * Decomposes pckt into a language and a message, storing them in
	 * this TranslationPacket
	 * @param pckt
	 * The string to be converted
	 * @throws PacketFormatException
	 * if pckt is badly formatted
	 */
	public TranslationPacket(String pckt) throws PacketFormatException {
		packet = pckt;
		pp = new PacketParser(packet);
		pp.accept(LOPEN);
		int uLen = pp.nextInt();
		pp.accept(")=");
		lang = pp.accept(uLen);
		pp.accept(TOPEN);
		int mLen = pp.nextInt();
		pp.accept(")=");
		msg = pp.accept(mLen);
	}
	
	/**
	 * Creates a translation packet from lng and text, storing
	 * their packet representation in packet.
	 * @param lng
	 * the desired language of the translation
	 * @param text
	 * the text to be translated
	 */
	public TranslationPacket(String lng, String text) {
		lang = lng;
		msg = text;
		StringBuilder sb = new StringBuilder();
	    sb.append(LOPEN).append(lng.length()).append(CLOSE).append(lng);
	    sb.append(TOPEN).append(text.length()).append(CLOSE).append(text);
	    packet = sb.toString();
	}

	/**
	 * @return
	 * The desired language of this packet
	 */
	public String getLanguage() {
		return lang;
	}

	/**
	 * @return
	 * The relevant data in this packet
	 */
	public String getMessage() {
		return msg;
	}

	/**
	 * @return
	 * The one-string representation of this packet
	 */
	public String getPacket() {
		return packet;
	}
	
}
