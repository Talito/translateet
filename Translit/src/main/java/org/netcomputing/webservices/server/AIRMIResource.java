package org.netcomputing.webservices.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.netcomputing.webservices.datamodel.Text;

import rmi.client.LaunchCalcTranslationtask;

/**
 * @TODO: Write explanation of REST API and AI interaction
 * @author Jose and Stijn
 *
 */
//Will map the resource to the URL positions
@Path("/airmi")
public class AIRMIResource {
	@Context UriInfo uriInfo;
	@Context
	Request request;
	
	//TextDAO tDAO = new TextDAO();
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	/** 
	 * Method that hides the logic to process a request by the AI
	 * @param uid in the web path
	 * @return text with the given uid from the database
	 */
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newTranslationRequest(@FormParam("textuid") String uid,
			@FormParam("message") String message, @FormParam("langFrom") String langFrom, 
			@FormParam("langTo") String langTo) throws IOException {
		
		logger.log(Level.INFO, "AI called.");
		
//		new Thread(new Runnable() {
//			public void run() {
//				Translator tr = new Translator(langFrom, langTo);
//				tr.translate();
//			}
//		}).start();
		
		Text task = new Text();
		task.setUID(uid); task.setMessage(message);
		new LaunchCalcTranslationtask(task, langFrom, langTo).start();
		logger.log(Level.INFO, "LaunchCalcTranslationtask started and running already...");
		
//		DBQueue us = new DBQueue();
//		us.updateDatabase();
		
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
