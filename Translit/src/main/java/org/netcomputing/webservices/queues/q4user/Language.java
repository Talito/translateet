package org.netcomputing.webservices.queues.q4user;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Language {

	private static Map<String, Language> languages = 
			Collections.synchronizedMap(new HashMap<String, Language>());
	//TODO: Factory?
	public static Language getLanguage(String nam) {
		if(!languages.containsKey(nam)) {
			Language ret = new Language(nam);
			languages.put(nam, ret);
			return ret;
		}
		return languages.get(nam);
	}
	
	//TODO: configurable host
	private String host = "localhost";
	private String name;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private Language(String nam) {
		name = nam;
	}
	
	public void requestTranslation(String toLang, String text) {
			
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(host);
	    
	    try {
		    Connection connection = factory.newConnection();
		    
		    Channel channel = connection.createChannel();
		    //Create an exchange for this language
		    channel.exchangeDeclare(name, "direct");
		    
		    // Binding a queue isn't necessary, but prevents dropped packets
		    String queueName = channel.queueDeclare().getQueue();
		    channel.queueBind(queueName, name, toLang);
		    
		    //Encode the information
		    TranslationPacket tp = new TranslationPacket(toLang, text);
		    
		    //Use the goal languages as a routing key
		    channel.basicPublish(name, toLang, null, tp.getPacket().getBytes("UTF-8"));
		    
		    logger.log(Level.INFO, "Enqueued <" + text + ">, translate to <" + toLang + ">");
		    
		    channel.close();
		    connection.close();
	    } catch (IOException e) {
	    	logger.log(Level.SEVERE, "Sending <" + text + "> translate to <" + toLang
	    			+ "> failed.", e);
	    } catch (TimeoutException e) {
	    	logger.log(Level.SEVERE, "Sending <" + text + "> translate to <" + toLang
	    			+ "> timed out.", e);
	    }
		
	}
	
}
