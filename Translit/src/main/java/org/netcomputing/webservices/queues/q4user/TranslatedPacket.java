package org.netcomputing.webservices.queues.q4user;

public class TranslatedPacket extends TranslationPacket {

	private String fromLang;
	private String original;
	
	private static final String FOPEN = "From(l=";
	private static final String OOPEN = "Original(l=";
	
	public TranslatedPacket(String from, String to, String msg, String fromsg) {
		super(to, msg);
		fromLang = from;
		original = fromsg;
		StringBuilder sb = new StringBuilder(packet);
		sb.append(FOPEN).append(from.length()).append(CLOSE).append(from);
		sb.append(OOPEN).append(fromsg.length()).append(CLOSE).append(fromsg);
		packet = sb.toString();
	}
	
	public TranslatedPacket(String message) throws PacketFormatException {
		super(message);
		pp.accept(FOPEN);
		int fSize = pp.nextInt();
		pp.accept(CLOSE);
		fromLang = pp.accept(fSize);
		pp.accept(OOPEN);
		int oSize = pp.nextInt();
		pp.accept(CLOSE);
		original = pp.accept(oSize);
	}
	
	public String getFromLanguage() {
		return fromLang;
	}
	
	public String getOriginal() {
		return original;
	}
	
}
