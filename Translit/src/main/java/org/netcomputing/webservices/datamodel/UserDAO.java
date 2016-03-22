package org.netcomputing.webservices.datamodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.netcomputing.webservices.database.UserRepository;
import org.netcomputing.webservices.server.ConfigLoader;


/**
 * Implements the business logic layer (for example, applies 
 * algorithms on the data received from the repository; finally 
 * it serves the results to the "user-interface" layer.
 * @see https://github.com/spvenka/examples/blob/master/src/main/
 * java/com/examples/mongodb/service/impl/PersonServiceImpl.java
 */

public class UserDAO {

	/**
	 * The user repository implements the specific methods to communicate
	 * with the (specific) database, e.g. Mongo.
	 */
	private UserRepository ur;
	
	//private Map<String, User> contentProvider = new HashMap<String, User>();
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	public UserDAO() {
		ur = new UserRepository();
		try {
			logger.log(Level.INFO, "UserDAO constructor called...");
			ur.initializeUserRepository(new ConfigLoader());
			logger.log(Level.INFO, "UserDAO initialized.");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "initializeUserRepository did not work");
			e.printStackTrace();
		}
	}

	public User getUser(String uid) {
		return ur.getUserByUID(uid);
	}
	
	public void addUser(User user) {
		logger.log(Level.INFO, "addUser in UserDAO called.");
		ur.createUser(user);
	}

	public List<User> getAllUsers() {
		ArrayList<User> allUsers = new ArrayList<User>();
		allUsers = ur.getAllUsers();
		return allUsers;
	}

	public User getUserWithHighestScore() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateUser(User user) {
		ur.updateUser(user);
	}

	public void deleteUser(User fakeUser) {
		ur.deleteUser(fakeUser.getUID());
	}

}