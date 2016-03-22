package org.netcomputing.webservices.database;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.netcomputing.webservices.datamodel.Text;
import org.netcomputing.webservices.server.ConfigLoader;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

/**
 * 
 * @author Jose and Stijn
 * @version 0.2.0
 */
public class TextRepository {
	     
	/**
	 * The abstraction of a (concrete) database.
	 */
	private MongoDatabase db ;
	
	/**
	 * The mongoClient that connects to a database instance
	 * @see https://docs.mongodb.org/getting-started/java/client/
	 */
	private MongoClient mongoClient;

	/**
	 * The variable that abstracts the loading of the configuration file.
	 * By the time UserRepository is created, ConfigLoader should already
	 * be set up.
	 */
	private ConfigLoader configLoader;
	
	/**
	 * The name of the collection hard-coded. It could be wise to think 
	 * about its convenience to be added to the configLoader.
	 */
	private String collectionName = "texts";
	
	private MongoCollection<BasicDBObject> textsCollection;

	Logger logger = Logger.getLogger(this.getClass().getName());
	
	public TextRepository() {};

	/**
	 * Initializes the singleton object.
	 * @param configLoader holds the preloaded settings from an external file.
	 */
	public void initializeTextRepository(ConfigLoader configLoader) {
		logger.log(Level.INFO, "Starting initialization of TextRepository...");
		if (configLoader != null) {
			this.configLoader = configLoader;
			logger.log(Level.INFO, "ConfigLoader attacthed while initializing TextRepository...");
			try{
				logger.log(Level.INFO, "Trying initializing Mongo database...");
		        mongoClient = new MongoClient(this.configLoader.getDbAddress(), this.configLoader.getDbPort());
				// if the DB doesn't exist, Mongo will create it
		        db = mongoClient.getDatabase(this.configLoader.getDbName());
				textsCollection = db.getCollection(collectionName, BasicDBObject.class);
				logger.log(Level.INFO, "initializeTextRepository successfully finished.");
		    } catch(Exception e) {
		        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    }
		} else {
			logger.log(Level.SEVERE, "initializeTextRepository called with a null-valued configLoader.");
		}
	}
	
	
	/**
	 * 
	 * @param text uid cannot be null
	 * @return
	 */
	public Text getTextByUID(String uid) {
		logger.log(Level.INFO, "getTextByUID called in TextRepository.");
		
		BasicDBObject query = new BasicDBObject();
		query.put("UID", uid);
		FindIterable<BasicDBObject> cursor = textsCollection.find(query);
		BasicDBObject userQueried = cursor.first();
		
		Text text = new Text();
		if (userQueried == null) {
			logger.log(Level.INFO, "getTextByUID: text with uid " + uid + " not found.");
		} else {
			text.setUID(userQueried.getString("UID"));
			text.setMessage(userQueried.getString("message"));
			logger.log(Level.INFO, "getTextByUID: text with uid " + uid + " found. Data: \n "+ text.toString());
		}
		return text;
	}

	/**
	 * 
	 * @param text cannot be null
	 */
	public void createRequest(Text text) {
		logger.log(Level.INFO, "createTranslationRequest called in TextRepository.");
		if (text != null) {
			String jsonUser = "{\"UID\":\"" + text.getUID() + "\"," + "\"message\":\"" + text.getMessage() + "\"}";
			BasicDBObject jsonObject = (BasicDBObject) JSON.parse(jsonUser);
			textsCollection.insertOne(jsonObject);
		} else {
			logger.log(Level.SEVERE, "createTranslationRequest method called with null-valued text.");
		}
	}

	public ArrayList<Text> getAllTexts() {
		logger.log(Level.INFO, "getAllTexts called in TextRepository.");
		
		ArrayList<Text> allTexts = new ArrayList<Text>();
		FindIterable<BasicDBObject> cursor = textsCollection.find();
    	MongoCursor<BasicDBObject> e = cursor.iterator();
	    try {
	        while (e.hasNext()) {
	        	BasicDBObject currentText = e.next();
				Text text = new Text();
				text.setUID(currentText.getString("UID"));
				text.setMessage(currentText.getString("message"));
				
				allTexts.add(text);
	        }
	    } finally {
	        e.close();
	    }
		return allTexts;
	}
}
