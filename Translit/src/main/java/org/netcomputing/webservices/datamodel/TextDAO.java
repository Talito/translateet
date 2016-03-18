package org.netcomputing.webservices.datamodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.netcomputing.webservices.database.TextRepository;
import org.netcomputing.webservices.server.ConfigLoader;


public class TextDAO {

		private TextRepository tr;
		
		//private Map<String, User> contentProvider = new HashMap<String, User>();
		
		Logger logger = Logger.getLogger(org.netcomputing.webservices.datamodel.TextDAO.class.getName());
		
		public TextDAO() {
			tr = new TextRepository();
			try {
				logger.log(Level.INFO, "TextDAO constructor called...");
				tr.initializeTextRepository(new ConfigLoader());
				logger.log(Level.INFO, "TextDAO initialized.");
			} catch (IOException e) {
				logger.log(Level.SEVERE, "initializeUserRepository did not work");
				e.printStackTrace();
			}
		}

		public Text getText(String uid) {
			return tr.getTextByUID(uid);
		}
		
		public void addRequest(Text text) {
			logger.log(Level.INFO, "addText in TextDAO called.");
			tr.createRequest(text);
		}


		public List<Text> getAllTexts() {
			ArrayList<Text> allTexts = new ArrayList<Text>();
			allTexts = tr.getAllTexts();
			return allTexts;
		}
}