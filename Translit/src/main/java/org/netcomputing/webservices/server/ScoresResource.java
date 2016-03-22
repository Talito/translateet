package org.netcomputing.webservices.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.netcomputing.webservices.datamodel.User;
import org.netcomputing.webservices.datamodel.UserDAO;


//Will map the resource to the URL positions
@Path("/scores")
public class ScoresResource {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo 
	
	@Context UriInfo uriInfo;
	@Context
	Request request;
	
	UserDAO uDAO = new UserDAO();
	
	Logger logger = Logger.getLogger(this.getClass().getName());

	@GET
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public User HighestRankedUser() {
		logger.log(Level.INFO, "highestRankedUser called.");
		User u = uDAO.getUserWithHighestScore();
		if (u == null) {
			throw new RuntimeException("GET: user was not found. Empty database?");			
		}
		return u;
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void giveUserScore(@FormParam("uid") String uid,
			@FormParam("score") String score) throws IOException {
		logger.log(Level.INFO, "giveUserScore called.");
		User user = new User();
		UsersResource ur = new UsersResource();
		System.out.println(uid);
		user = ur.getUserProfile(uid);
		user.setScore(Integer.parseInt(score));
		ur.updateUser(user);
		logger.log(Level.INFO, "" + user.getName() + "'s score updated.");
	}

}
