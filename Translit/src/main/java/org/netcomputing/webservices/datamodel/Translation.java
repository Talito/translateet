package org.netcomputing.webservices.datamodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement // Only needed if we also want to generate XML responses
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"UID", "originalTextUID", "score", "message", "translatorUID"})
public class Translation {
	@XmlElement(required = true)
	private long UID;
	@XmlElement(required = true)
	private String originalTçextUI;
	@XmlElement(required = true)
	private String message;
	private int score;
	@XmlElement(required = true)
	private String translatorUID;
	
	public long getUID() {
		return UID;
	}
	public void setUID(long UID) {
		this.UID = UID;
	}
	
	public String getText() {
		return originalTçextUI;
	}
	public void setText(String newTextUID) {
		this.originalTçextUI = newTextUID;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getTranslatorUID() {
		return translatorUID;
	}
	public void setTranslator(String theTranslatorUID) {
		this.translatorUID = theTranslatorUID;
	}

}
