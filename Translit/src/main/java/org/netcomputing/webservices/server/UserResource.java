package org.netcomputing.webservices.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.netcomputing.webservices.datamodel.User;
import org.netcomputing.webservices.datamodel.UserDAO;

@Path("/users/")
public class UserResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;
	
	Logger logger = Logger.getLogger(org.netcomputing.webservices.database.UserRepository.class.getName());
	
	public UserResource(UriInfo uriInfo, Request request, String id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}
	
	// For the browser	
	/** 
	 * Whatever
	 */
	//@GET
	//@Produces({"MediaType.TEXT_HTML"})
	/*public void showFormulier( @Context HttpServletResponse servletResponse){
		String t = "formulier.html";
		try {
			servletResponse.sendRedirect(t);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not redirect to: " + t);
			e.printStackTrace();
		}
	}*/
	
	
	/** 
	 * Method that hides the logic to search in the database users with the given UID
	 * @param uid in the web path
	 * @return user with the given uid from the database
	 */
	@Path("{uid}")
	@GET
	@Produces({"MediaType.TEXT_XML", "MediaType.TEXT_JSON"})
	public User getUserProfile(@PathParam("uid") String uid) {
		System.out.println("TEST");
		logger.log(Level.SEVERE, "getUserProfile called.");
	    if(uid == null || uid.trim().length() == 0) {
	        throw new RuntimeException("GET: there was no given valid unique identifier.");
	    }
		User u = UserDAO.instance.getUser(uid);
		if (u == null) {
			throw new RuntimeException("GET: user with given " + uid + " not found.");			
		}
		return u;
	}
	
	/** 
	 * Method that hides the logic to search in the database users with the given UID
	 * @param uid is taken from the form
	 * @return user
	 */
	/*@POST
	@Consumes("APPLICATION/X-WWW-FORM-URLENCODED")
	@Produces({"MediaType.TEXT_XML, qs=0.9", "MediaType.APPLICATION_JSON"})
	public User getUserProfileFromForm(@FormParam("uid") String uid) {
		logger.log(Level.INFO, "getUserProfileFromForm called.");
	    if(uid == null || uid.trim().length() == 0) {
	        throw new RuntimeException("FORMPARAM-POST: there was no given valid unique identifier.");
	    }
		User u = UserDAO.instance.getUser(uid);
		if (u == null) {
			throw new RuntimeException("FORMPARAM-POST: user with given " + uid + " not found.");			
		}
		return u;
	}*/
	
	/** 
	 * Method that hides the logic to search in the database users with the given score
	 * @param score in the web path
	 * @return array of users
	 */
	/*@Path("{score}")
	@GET
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public ArrayList<User> getUsersByScore(@PathParam("score") int score) {
		ArrayList<User> users = UserDAO.instance.getUsersByScore(score);
		if(users==null)
			throw new RuntimeException("GET: text with key words " + id +  ", not found");
		return users;
	}*/
	
} 