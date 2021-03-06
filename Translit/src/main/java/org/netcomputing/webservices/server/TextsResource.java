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
import org.netcomputing.webservices.queues.q4user.DBQueue;
import org.netcomputing.webservices.queues.q4user.Language;
import org.netcomputing.webservices.queues.q4user.Translator;


//Will map the resource to the URL positions
@Path("/texts")
public class TextsResource {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo 
	
	@Context UriInfo uriInfo;
	@Context
	Request request;
	
	TextDAO tDAO = new TextDAO();
	
	Logger logger = Logger.getLogger(this.getClass().getName());

	// Return the list of events to the user in the browser 
	@GET
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public List<Text> getTextsBrowser() {
		logger.log(Level.INFO, "getTextsBrowser called.");
		List<Text> texts = new ArrayList<Text>();
		return texts;
	}
	
	/** 
	 * @param uid in the web path
	 * @return text with the given uid from the database
	 */
	@Path("{uid}")
	@GET
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public Text getTranslationRequest(@PathParam("uid") String uid) {
		logger.log(Level.INFO, "getTranslationRequest called.");
	    if(uid == null || uid.trim().length() == 0) {
	        throw new RuntimeException("GET: there was no given valid unique identifier.");
	    }
		Text t = tDAO.getText(uid);
		if (t == null) {
			throw new RuntimeException("GET: text with given " + uid + " not found.");			
		}
		return t;
	}
	
	/** 
	 * Method that hides the logic to search in the database text with the given UID
	 * @param uid in the web path
	 * @return text with the given uid from the database
	 */
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newTranslationRequest(@FormParam("textuid") String uid,
			@FormParam("message") String message, @FormParam("langFrom") String langFrom, 
			@FormParam("langTo") String langTo) throws IOException {
		logger.log(Level.INFO, "newTranslationRequest called.");
		
		new Thread(new Runnable() {
			public void run() {
				Translator tr = new Translator(langFrom, langTo);
				tr.translate();
			}
		}).start();
		
		logger.log(Level.INFO, "Thread with Translator initialized.");
		new Thread(new Runnable() {
			public void run() {
				Language lang = Language.getLanguage(langFrom);
				lang.requestTranslation(langTo, message);
			}
		}).start();
		logger.log(Level.INFO, "Thread with Language initialized.");
		
		DBQueue us = new DBQueue();
		us.updateDatabase();
		logger.log(Level.INFO, "DBQueue update() called...");
		
		/**
		 * We should be doing some writing in the DB here?
		 */
		/*Text text = new Text();
		text.setUID(uid);
		text.setMessage(message);
		System.out.println(uid);
		System.out.println(message);
		tDAO.addRequest(text);*/
	}
}
