package org.netcomputing.webservices.datamodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement // Only needed if we also want to generate XML responses
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"UID", "name", "score", "translation"})
public class User {
	
	@XmlElement(name = "UID")
	private String UID; // to be later changed to Long?
	
	@XmlElement(name = "name")
	private String name;
	
	@XmlElement(name = "score")
	private int score;
	
	//@XmlElement(name = "translations")
	//private ArrayList<Translation> translation;	
	
	public User() {}
	
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
	/*public ArrayList<Translation> getTranslations() {
		return translation;
	}
	public void setTranslations(ArrayList<Translation> translation) {
		this.translation = translation;
	}*/
	
	public String toString() {
		return "User with UID: " + this.UID + ", name: " + this.name + ", and score: " + this.score + ".";
	}

}