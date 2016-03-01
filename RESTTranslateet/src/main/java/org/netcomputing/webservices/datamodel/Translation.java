package org.netcomputing.webservices.datamodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement // Only needed if we also want to generate XML responses
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "UID", "message", "score", "translation"})
public class Translation {
	
	private long UID;
	private String text;
	private int score;
	private User translator;
	
	public long getUID() {
		return UID;
	}
	public void setUID(long UID) {
		this.UID = UID;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public User getTranslator() {
		return translator;
	}
	public void setTranslator(User translator) {
		this.translator = translator;
	}

}
