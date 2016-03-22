package org.netcomputing.webservices.database;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.netcomputing.webservices.datamodel.User;
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
public class UserRepository {
	     
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
	private String collectionName = "users";
	
	private MongoCollection<BasicDBObject> usersCollection;

	Logger logger = Logger.getLogger(this.getClass().getName());
	
	public UserRepository() {};

	/**
	 * Initializes the singleton object.
	 * @param configLoader holds the preloaded settings from an external file.
	 */
	public void initializeUserRepository(ConfigLoader configLoader) {
		logger.log(Level.INFO, "Starting initialization of UserRepository...");
		if (configLoader != null) {
			this.configLoader = configLoader;
			logger.log(Level.INFO, "ConfigLoader attacthed while initializing UserRepository...");
			try{
				logger.log(Level.INFO, "Trying initializing Mongo database...");
		        mongoClient = new MongoClient(this.configLoader.getDbAddress(), this.configLoader.getDbPort());
				// if the DB doesn't exist, Mongo will create it
		        db = mongoClient.getDatabase(this.configLoader.getDbName());
				usersCollection = db.getCollection(collectionName, BasicDBObject.class);
				logger.log(Level.INFO, "initializeUserRepository successfully finished.");
		    } catch(Exception e) {
		        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    }
		} else {
			logger.log(Level.SEVERE, "initializeUserRepository called with a null-valued configLoader.");
		}
	}
	
	/**
	 * 
	 * @param user cannot be null.
	 */
	public void createUser(User user) {
		logger.log(Level.INFO, "createUser called in UserRepository.");
		if (user != null) {
			String jsonUser = "{\"UID\":\"" + user.getUID() + "\"," + "\"name\":\"" + user.getName() + "\","
			+ "\"score\":\"" + user.getScore()+ "\","
					+ "\"translations\":\"" + user.getTranslations() + "\"}";
			BasicDBObject jsonObject = (BasicDBObject) JSON.parse(jsonUser);
			usersCollection.insertOne(jsonObject);
		} else {
			logger.log(Level.SEVERE, "createUser method called with null-valued user.");
		}
	}
	
	/**
	 * 
	 * @param user cannot be null. Its UID is automatically generated by Mongo.
	 */
	public User getUserByUID(String uid) {
		logger.log(Level.INFO, "getUser called in UserRepository.");
		
		BasicDBObject query = new BasicDBObject();
		query.put("UID", uid);
		FindIterable<BasicDBObject> cursor = usersCollection.find(query);
		BasicDBObject userQueried = cursor.first();
		
		User user = new User();
		if (userQueried == null) {
			logger.log(Level.INFO, "getUser: user with uid " + uid + " not found.");
		} else {
			user.setUID(userQueried.getString("UID"));
			user.setName(userQueried.getString("name"));
			user.setScore(Integer.parseInt(userQueried.getString("score")));
			String queriedUserTranaslations = userQueried.getString("translations");
			String[] trans;
			trans = queriedUserTranaslations.split(",");
			ArrayList<String> transToList = new ArrayList<String>(trans.length);
			int i;
			for (i = 0; i < trans.length; i++) {
				transToList.add(trans[i]);
			}
			user.setTranslations(transToList);
			
			logger.log(Level.INFO, "getUser: user with uid " + uid + " found. Data: \n "+ user.toString());
		}
		return user;
	}
	
	/**
	 * 
	 * @param usersCollection
	 * @return
	 */
	public ArrayList<User> getAllUsers() {
		logger.log(Level.INFO, "getAllUsers called in UserRepository.");
		
		ArrayList<User> allUsers = new ArrayList<User>();
		FindIterable<BasicDBObject> cursor = usersCollection.find();
    	MongoCursor<BasicDBObject> e = cursor.iterator();
	    try {
	        while (e.hasNext()) {
	        	BasicDBObject currentUser = e.next();
				User user = new User();
				user.setUID(currentUser.getString("UID"));
				user.setName(currentUser.getString("name"));
				user.setScore(Integer.parseInt(currentUser.getString("score")));
				String queriedUserTranaslations = currentUser.getString("translations");
				
				String[] trans;
				trans = queriedUserTranaslations.split(",");
				ArrayList<String> transToList = new ArrayList<String>(trans.length);
				int i;
				for (i = 0; i < trans.length; i++) {
					transToList.add(trans[i]);
				}
				user.setTranslations(transToList);
				
				allUsers.add(user);
	        }
	    } finally {
	        e.close();
	    }
		return allUsers;
	}

	/*
	public MongoDatabase getDB() throws UnknownHostException {
		if (mongoClient == null) {
			mongoClient = new MongoClient(configLoader.getDbAddress(), Integer.parseInt(configLoader.getDbPort()));
		}
		if (db == null) {
			db = mongoClient.getDatabase(configLoader.getDbName());
		}
		return db;
	}*/

}
