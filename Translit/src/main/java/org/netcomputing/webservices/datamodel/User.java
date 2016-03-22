package org.netcomputing.webservices.datamodel;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement // Only needed if we also want to generate XML responses
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"UID", "name", "score", "translations"})
public class User {
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	@XmlElement(name = "UID")
	private String UID;
	
	@XmlElement(name = "name")
	private String name;
	
	@XmlElement(name = "score")
	private int score;
	
	@XmlElement(name = "translations")
	private ArrayList<String> translations;	
	
	public User() {
		score = 0;
	}
	
	public String getUID() {
		return UID;
	}
	public void setUID(String id) {
		this.UID = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public ArrayList<String> getTranslations() {
		return translations;
	}

	
	public String toString() {
		return "User with UID: " + this.UID + ", name: " + this.name + ", and score: " + this.score + "."
				+ " Translations: " + translations.toString() + "\n";
	}

	public void setTranslations(ArrayList<String> trans) {	
		this.translations = trans;	
	}

}
