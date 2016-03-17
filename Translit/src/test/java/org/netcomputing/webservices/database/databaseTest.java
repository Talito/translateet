package org.netcomputing.webservices.database;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.netcomputing.webservices.datamodel.User;
import org.netcomputing.webservices.server.ConfigLoader;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import org.bson.Document;

public class databaseTest {

	private DB db ;
	private MongoClient mongoClient;
	private String dbName = "Translateet";
	private String collectionName = "users";
	private String dbAddress = "localhost";
	private int port = 27017;
	private DBCollection usersCollection;
	
	Logger logger = Logger.getLogger(org.netcomputing.webservices.database.UserRepository.class.getName());
	
	public databaseTest() {};

	public void initializeUserRepository() {
			DB database = mongoClient.getDB(dbName); // if the DB doesn't exist, Mongo will create it
			usersCollection = database.getCollection(collectionName);
			logger.log(Level.SEVERE, "initializeUserRepository called with a null-valued configLoader.");
	}

	public void createUser(User user) {
		logger.log(Level.INFO, "createUser called in databaseTest class.");
		if (user != null) {
			BasicDBObject document = new BasicDBObject();
			document.put("uid", user.getUID());
			document.put("name",  user.getName());
			document.put("score",  user.getScore());
			String[] translations = {"Translation 1", "Translation 2"};
			document.put("translations",  translations);
			String jsonUser = "{\"uid\":\"" + user.getUID() +"\","+"\"name\":\""+user.getName()+"\","+"\""
					+ "\"score\":\"" + user.getScore()+ "\"," + "\""
					+ "\"translations\":\"" + translations + "\"}";
			DBObject jsonObject = (DBObject) JSON.parse(jsonUser);
			usersCollection.insert(jsonObject);
			
		} else {
			logger.log(Level.SEVERE, "createUser method called with null-valued user.");
		}
	}

	public User getUser(String uid) {
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("uid", uid);
		DBCursor cursor = usersCollection.find(searchQuery);
		User user = (User) cursor.next();
		logger.log(Level.INFO, "User retrieved from Users collection: " + user.getName());
		return user;
	}

	public ArrayList<User> getAllUsers() {
		ArrayList<User> allUsers = null;
		DBCursor curs = usersCollection.find();
		return allUsers;
	}

	public void addUserToDB(User user) {
	//
	}


}
