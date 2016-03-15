package org.netcomputing.webservices.datamodel;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement // Only needed if we also want to generate XML responses
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "UID", "message", "translation"})
public class Text {
	@XmlElement(required = true)
	private long UID;
	
	@XmlElement(required = true)
	private String message;
	
	@XmlElementWrapper(name="translations")
	@XmlElement(required = false)
	private ArrayList<Translation> translation;
	
	public long getUID() {
		return UID;
	}
	public void setUID(long UID) {
		this.UID = UID;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<Translation> getTranslations() {
		return translation;
	}
	public void setTranslations(ArrayList<Translation> translation) {
		this.translation = translation;
	}
}