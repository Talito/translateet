package org.netcomputing.webservices.queues.q4user;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.netcomputing.webservices.server.ConfigLoader;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * Container for database interactions. Collects any translation sent
 * by translators, and stores it in the database.
 */
public class DBQueue {
	
	/**
	 * Stores the configuration of the database (port, name, address).
	 */
	private ConfigLoader configLoader;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private String host = "localhost"; // default
	public static String DB;
	
	public DBQueue() {
		if (configLoader == null) {
			try {
				configLoader = new ConfigLoader();
				DB = this.configLoader.getDbName();
				host = this.configLoader.getDbAddress();
				logger.log(Level.INFO, "ConfigLoader in DBQueue - DB name: " + DB + ", host: "
						+ host + ".");
			} catch (IOException e) {
				logger.log(Level.INFO, "Could not set the variable ConfigLoader in DBQueue.");
				e.printStackTrace();
			}
		}
	}
	
	public void updateDatabase() {
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(host);
	    try {
	        Connection connection = factory.newConnection();
	        Channel channel = connection.createChannel();
	        
	        //Create a queue to accept DB requests
	        channel.queueDeclare(DB, true, false, false, null);
	        
	        logger.log(Level.INFO, "Waiting for translations");
	        
		    Consumer consumer = new DefaultConsumer(channel) {
			    @Override
			    public void handleDelivery(String consumerTag, Envelope envelope,
			                               AMQP.BasicProperties properties, byte[] body) throws IOException {
				    String message = new String(body, "UTF-8");
				    logger.log(Level.INFO, "Received message:" + message);
				    TranslatedPacket tp;
					try {
						//interpret received message
						tp = new TranslatedPacket(message);
					    logger.log(Level.INFO, tp.getFromLanguage() + ">" + tp.getLanguage() + 
					    		" " + tp.getOriginal() + ">" + tp.getMessage());
					    //TODO: Update DB
					} catch (PacketFormatException e) {
						logger.log(Level.SEVERE, "Package malformed. Package: " + message +
								"Problem: " + e.getMessage(), e);
					}
			    }
		    };
		    channel.basicConsume(DB, true, consumer);
		    
	    } catch (IOException e) {
	    	logger.log(Level.SEVERE, "Reception failed", e);
	    } catch (TimeoutException e) {
	    	logger.log(Level.SEVERE, "Reception timed out", e);
	    }
	}
	
}
