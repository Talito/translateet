package org.netcomputing.webservices.queues.q4user;

public class TranslationPacket {

	private String user;
	private String msg;
	private String packet;
	
	private static String close = ")=";
	private static String uOpen = "User(l=";
	private static String tOpen = "Message(l=";
	
	public TranslationPacket(String pckt) throws PacketFormatException {
		packet = pckt;
		PacketParser pp = new PacketParser(packet);
		pp.accept(uOpen);
		int uLen = pp.nextInt();
		pp.accept(")=");
		user = pp.accept(uLen);
		pp.accept(tOpen);
		int mLen = pp.nextInt();
		pp.accept(")=");
		msg = pp.accept(mLen);
	}
	
	public TranslationPacket(String uid, String text) {
		user = uid;
		msg = text;
		StringBuilder sb = new StringBuilder();
	    sb.append(uOpen).append(uid.length()).append(close).append(uid);
	    sb.append(tOpen).append(text.length()).append(close).append(text);
	    packet = sb.toString();
	}

	public String getUser() {
		return user;
	}

	public String getMsg() {
		return msg;
	}

	public String getPacket() {
		return packet;
	}
	
}
