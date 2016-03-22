package org.netcomputing.webservices.datamodel;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement // Only needed if we also want to generate XML responses
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "UID", "message", "translations"})
public class Text implements Serializable{
	
	private static final long serialVersionUID = 3942967283733335111L;
	
	@XmlElement(required = true)
	private String UID;
	
	@XmlElement(required = true)
	private String message;
	
	@XmlElementWrapper(name="translations")
	@XmlElement(required = false)
	private ArrayList<Translation> translations;
	
	public String getUID() {
		return UID;
	}
	public void setUID(String UID) {
		this.UID = UID;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<Translation> getTranslations() {
		return translations;
	}
	public void setTranslations(ArrayList<Translation> translations) {
		this.translations = translations;
	}
}
