package org.netcomputing.webservices.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.netcomputing.webservices.datamodel.User;
import org.netcomputing.webservices.datamodel.UserDAO;


//Will map the resource to the URL positions
@Path("/profile")
public class ProfileManager {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo 
	
	@Context UriInfo uriInfo;
	@Context
	Request request;
	
	UserDAO uDAO = new UserDAO();
	
	Logger logger = Logger.getLogger(this.getClass().getName());

	@GET
	@Produces({MediaType.TEXT_PLAIN})
	public Response deleteMyself() {
		logger.log(Level.INFO, "deleteMyself called.");
		// Test purposes, fake user
		User fakeUser = new User();
		fakeUser.setUID("333"); fakeUser.setName("FakeName");
		uDAO.deleteUser(fakeUser);
		return Response.ok().build();
	}
	
	

}
