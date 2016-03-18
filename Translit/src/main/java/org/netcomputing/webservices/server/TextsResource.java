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

import org.netcomputing.webservices.datamodel.Text;
import org.netcomputing.webservices.datamodel.TextDAO;
import org.netcomputing.webservices.datamodel.User;
import org.netcomputing.webservices.datamodel.UserDAO;


//Will map the resource to the URL positions
@Path("/texts")
public class TextsResource {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo 
	
	@Context UriInfo uriInfo;
	@Context
	Request request;
	
	UserDAO uDAO = new UserDAO();
	
	Logger logger = Logger.getLogger(org.netcomputing.webservices.database.UserRepository.class.getName());

	// Return the list of events to the user in the browser 
	@GET
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public List<Text> getTextsBrowser() {
		logger.log(Level.INFO, "getUserBrowser called.");
		List<Text> texts = new ArrayList<Text>();
		texts.addAll(TextDAO.instance.getModel().values());
		return texts;
	}
	
	/** 
	 * @param uid in the web path
	 * @return text with the given uid from the database
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
	public void newText(@FormParam("uid") String uid,
			@FormParam("message") String message) throws IOException {
		logger.log(Level.INFO, "newMessage called.");
		Text text = new Text();
		text.setMessage(message);
		TextDAO.instance.getModel().put(uid, text);
	}
}
