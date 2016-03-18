package org.netcomputing.webservices.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.netcomputing.webservices.datamodel.User;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;


public class Main {
	
	static Logger logger = Logger.getLogger(org.netcomputing.webservices.database.TextRepository.class.getName());
	
	public static void createUser(User user, MongoCollection<BasicDBObject> usersCollection) {
		logger.log(Level.INFO, "createUser called in Main test.");
		
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
	
	public static void retrieveUser(String name, MongoCollection<BasicDBObject> usersCollection) {
		logger.log(Level.INFO, "retrieveUser called in Main test.");
		
		BasicDBObject query = new BasicDBObject();
		query.put("name", name);
		FindIterable<BasicDBObject> cursor = usersCollection.find(query);
		BasicDBObject userQueried = cursor.first();
		
		if (userQueried == null) {
			logger.log(Level.INFO, "retrieveUser: user " + name + " not found.");
		} else {
			User user = new User();
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
			
			logger.log(Level.INFO, "retrieveUser: user " + name + " found. Data: \n "+ user.toString());
		}
	}
	
	public static ArrayList<User> getAllUsers(MongoCollection<BasicDBObject> usersCollection) {
		logger.log(Level.INFO, "allUsers called in Main test.");
		
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
	
	public static void main(String args[]) throws IOException {
		MongoDatabase db;
		MongoClient mongoClient;
		String dbName = "Translateet";
		String collectionName = "users";
		MongoCollection<BasicDBObject> usersCollection;
		
		ArrayList<String> trans = new ArrayList<String>(2);
		trans.add("Trans1"); trans.add("Trans2");
		ArrayList<String> trans2 = new ArrayList<String>(2);
		trans2.add("Trans3"); trans2.add("Trans4");
		User u1 = new User(); u1.setUID("001"); u1.setName("Jasel"); u1.setScore(7); u1.setTranslations(trans);
		User u2 = new User(); u2.setUID("002"); u2.setName("Timmy"); u2.setScore(5); u2.setTranslations(trans2);
	    try{
	        mongoClient = new MongoClient("localhost", 27017);
	        db = mongoClient.getDatabase(dbName);
			usersCollection = db.getCollection(collectionName, BasicDBObject.class);
			createUser(u1, usersCollection);
			createUser(u2, usersCollection);
			retrieveUser("Jasel", usersCollection);
			retrieveUser("Nonexist", usersCollection);
			System.out.println("Finishing...");
			System.out.println(getAllUsers(usersCollection));
	    } catch(Exception e) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    }
	}
}
