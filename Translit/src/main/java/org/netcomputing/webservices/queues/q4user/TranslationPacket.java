package org.netcomputing.webservices.queues.q4user;

public class TranslationPacket {

	private String lang;
	private String msg;
	protected String packet;
	protected PacketParser pp;
	
	protected static final String CLOSE = ")=";
	protected static final String LOPEN = "Language(l=";
	protected static final String TOPEN = "Message(l=";
	
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
	
	public TranslationPacket(String lng, String text) {
		lang = lng;
		msg = text;
		StringBuilder sb = new StringBuilder();
	    sb.append(LOPEN).append(lng.length()).append(CLOSE).append(lng);
	    sb.append(TOPEN).append(text.length()).append(CLOSE).append(text);
	    packet = sb.toString();
	}

	public String getLanguage() {
		return lang;
	}

	public String getMessage() {
		return msg;
	}

	public String getPacket() {
		return packet;
	}
	
}
