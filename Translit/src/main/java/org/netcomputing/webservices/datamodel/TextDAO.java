package org.netcomputing.webservices.datamodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton design pattern The possible implementation of Java depends on the
 * version of Java you are using. As of Java 6 you can define singletons with a
 * single-element enum type. This way is currently the best way to implement a
 * singleton in Java 1.6 or later according to the book ""Effective Java from
 * Joshua Bloch.
 */

public enum TextDAO {
	instance;

	private Map<Long, Text> contentProvider = new HashMap<Long, Text>();

	private TextDAO() {
		// to change later. modify XML?
		Text t = new Text();
		contentProvider.put((long)1, t);
	}

	public Map<Long, Text> getModel() {
		return contentProvider;
	}

}