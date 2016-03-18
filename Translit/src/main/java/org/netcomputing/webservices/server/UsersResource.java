package org.netcomputing.webservices.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.netcomputing.webservices.datamodel.User;
import org.netcomputing.webservices.datamodel.UserDAO;


//Will map the resource to the URL positions
@Path("/users")
public class UsersResource {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo 
	
	@Context UriInfo uriInfo;
	@Context
	Request request;
	
	UserDAO uDAO = new UserDAO();
	
	Logger logger = Logger.getLogger(org.netcomputing.webservices.database.TextRepository.class.getName());

	// Return the list of events to the user in the browser 
	@GET
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public List<User> getUsersBrowser() {
		logger.log(Level.INFO, "getUsersBrowser called.");
		List<User> users = new ArrayList<User>();
		users = uDAO.getAllUsers();
		return users;
	}
	
	/** 
	 * Method that hides the logic to search in the database users with the given UID
	 * @param uid in the web path
	 * @return user with the given uid from the database
	 */
	@Path("{uid}")
	@GET
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public User getUserProfile(@PathParam("uid") String uid) {
		logger.log(Level.INFO, "getUserProfile called.");
	    if(uid == null || uid.trim().length() == 0) {
	        throw new RuntimeException("GET: there was no given valid unique identifier.");
	    }
		User u = uDAO.getUser(uid);
		if (u == null) {
			throw new RuntimeException("GET: user with given " + uid + " not found.");			
		}
		return u;
	}
	
	/** 
	 * Method that hides the logic to search in the database users with the given UID
	 * @param uid in the web path
	 * @return user with the given uid from the database
	 */
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newUser(@FormParam("uid") String uid,
			@FormParam("name") String name) throws IOException {
		logger.log(Level.INFO, "newUser called.");
		User user = new User();
		user.setUID(uid); // Long.parseLong(id);
		user.setName(name);
		uDAO.addUser(user);
		//UserDAO.instance.getModel().put(uid, user);
		
	}

	// Defines that the next path parameter after Events is
	// treated as a parameter and passed to the EventResources
	// Allows to type
	// http://localhost:8080/.../rest/locations/1
	// 1 will be treaded as parameter and passed to EventResource
	@Path("{event}")
	public EventResource getLocation(@PathParam("event") String id) {
		logger.log(Level.INFO, "getLocation called.");
		return new EventResource(uriInfo, request, id);
	}
}
