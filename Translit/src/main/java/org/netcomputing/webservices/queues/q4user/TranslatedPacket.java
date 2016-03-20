package org.netcomputing.webservices.queues.q4user;

public class TranslatedPacket extends TranslationPacket {

	private String fromLang;
	
	private static final String FOPEN = "From(l=";
	
	public TranslatedPacket(String from, String to, String msg) {
		super(to, msg);
		fromLang = from;
		StringBuilder sb = new StringBuilder(packet);
		sb.append(FOPEN).append(from.length()).append(CLOSE).append(from);
		packet = sb.toString();
	}
	
	public TranslatedPacket(String message) throws PacketFormatException {
		super(message);
		pp.accept(FOPEN);
		int fSize = pp.nextInt();
		pp.accept(CLOSE);
		fromLang = pp.accept(fSize);
	}
	
	public String getFromLanguage() {
		return fromLang;
	}
	
}
