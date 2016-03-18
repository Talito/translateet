package org.netcomputing.webservices.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Loads the information from a file (e.g. config.lua) and
 * stored statically in the class, so other classes might
 * use this information (e.g. database address).
 * For design issues (static final vs final attributes):
 * @see http://stackoverflow.com/questions/26908854/
 * what-is-the-best-way-of-reading-configuration-parameters-from-configuration-file
 */
public final class ConfigLoader {
	
	Logger logger = Logger.getLogger(org.netcomputing.webservices.server.ConfigLoader.class.getName());
	
	/**************************************
	 ** VALUES TO CONFIGURE THE DATABASE ** 
	 **************************************/
	/**
	 * Server host address
	 */
	public final String dbAddress;
	
	/**
	 * Server port. Usually 27017
	 */
	public final String dbPort;
	
	/**
	 * Database name
	 */
	public final String dbName;
	
	public String getDbAddress() {
		return dbAddress;
	}

	/**
	 * @return the port as an integer, as it will be used along the program in that way
	 */
	public int getDbPort() {
		return Integer.parseInt(dbPort);
	}

	public String getDbName() {
		return dbName;
	}

	/**
	 * Constructor that attempts to load the config. file
	 * and initializes all the variables
	 * @throws IOException 
	 */
	public ConfigLoader() throws IOException {
		logger.log(Level.INFO, "ConfigLoader constructor called.");
		InputStream inputStream = null;
		Properties prop = new Properties();
		String propFileName = "mongodb.properties";
		try {
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("Settings for MongoDB '" + propFileName + "' "
						+ "not found in the classpath");
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			dbName = prop.getProperty("dbName");
			dbAddress = prop.getProperty("dbAddress");
			dbPort = prop.getProperty("dbPort");
			inputStream.close();
		}
		logger.log(Level.INFO, "configLoader successfully built.");
	}


}
