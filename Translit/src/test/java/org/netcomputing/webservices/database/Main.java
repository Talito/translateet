package org.netcomputing.webservices.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.netcomputing.webservices.datamodel.User;
import org.netcomputing.webservices.server.ConfigLoader;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;


public class Main {
	static Logger logger = Logger.getLogger(org.netcomputing.webservices.database.UserRepository.class.getName());
	
	public static void createUser(User user, MongoCollection<BasicDBObject> usersCollection) {
		
		logger.log(Level.INFO, "createUser called in Main test.");
		
		
		/** TO FIX THE ARRAY OF TRANSLATIONS (?) **/
		if (user != null) {
			BasicDBObject document = new BasicDBObject();
			document.put("uid", user.getUID());
			document.put("name",  user.getName());
			document.put("score",  user.getScore());
			String[] translations = {"Translation 1", "Translation 2"};
			document.put("translations",  translations);
			String jsonUser = "{\"uid\":\"" + user.getUID() +"\","+"\"name\":\""+user.getName()+"\","
			+ "\"score\":\"" + user.getScore()+ "\","
					+ "\"translations\":\"" + translations.toString() + "\"}";
			BasicDBObject jsonObject = (BasicDBObject) JSON.parse(jsonUser);
			usersCollection.insertOne(jsonObject);
			
		} else {
			logger.log(Level.SEVERE, "createUser method called with null-valued user.");
		}
	}
	
	public static void main(String args[]) throws IOException {
		
		MongoDatabase db;
		MongoClient mongoClient;
		String dbName = "Translateet";
		String collectionName = "users";
		MongoCollection<BasicDBObject> usersCollection;
		
		User u1 = new User(); u1.setUID("001"); u1.setName("Jasel"); u1.setScore(7);
		User u2 = new User(); u2.setUID("002"); u2.setName("Timmy"); u2.setScore(5);		
		User u3 = new User();
		User u4 = new User();

	    try{
	        mongoClient = new MongoClient( "localhost" , 27017 );
	        db = mongoClient.getDatabase(dbName);
			usersCollection = db.getCollection(collectionName, BasicDBObject.class);
			createUser(u1, usersCollection);
	    } catch(Exception e) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    }
	}
}
