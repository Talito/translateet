package org.netcomputing.webservices.queues.q4user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
 * Represents a translator in the Queue section of the program
 * Presents a message to a translator, and sends the translation
 * to the server 
 */
public class Translator {
	
	private String langFrom;
	private String langTo;
	private String host = "localhost"; // default
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private ConfigLoader configLoader;
	
	public Translator() {
		if (configLoader != null) {
			try {
				configLoader = new ConfigLoader();
				host = this.configLoader.getDbAddress();
				logger.log(Level.INFO, "ConfigLoader in Translator - host: "
						+ host + ".");
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Could not set the variable ConfigLoader in Translator.");
				e.printStackTrace();
			}
		}
	}
	
	public Translator(String from, String to) {
		this();
		langFrom = from;
		langTo = to;
	}

	public void translate() {
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(host);
	    try {
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
	
		    channel.exchangeDeclare(langFrom, "direct");
		    String queueName = channel.queueDeclare().getQueue();
		    channel.queueBind(queueName, langFrom, langTo);
	
		    logger.log(Level.INFO, "Waiting to translate");
	
		    Consumer consumer = new DefaultConsumer(channel) {
			    @Override
			    public void handleDelivery(String consumerTag, Envelope envelope,
			                               AMQP.BasicProperties properties, byte[] body) throws IOException {
				    String message = new String(body, "UTF-8");
				    logger.log(Level.INFO, "Received " + message);
				        
				    ConnectionFactory factory = new ConnectionFactory();
				    factory.setHost(host);
				    try {
						Connection connection = factory.newConnection();
						Channel channel = connection.createChannel();
						
						TranslationPacket tp = new TranslationPacket(message);
						//translation logic goes here
						
						System.out.println("Please translate:" + tp.getMessage() + " to " + tp.getLanguage());
						BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
						String translation = bf.readLine();						
						bf.close();
												
						TranslatedPacket trans = new TranslatedPacket(langFrom, tp.getLanguage(),
								translation, tp.getMessage());
						
						channel.queueDeclare(DBQueue.DB, true, false, false, null);
						channel.basicPublish("", DBQueue.DB, null, trans.getPacket().getBytes("UTF-8"));
						
						channel.close();
						connection.close();
				    } catch (IOException e) {
				    	logger.log(Level.SEVERE, "Reception failed", e);
					} catch (TimeoutException e) {
						logger.log(Level.SEVERE, "Reception timed out", e);
					} catch (PacketFormatException e) {
						logger.log(Level.SEVERE, "Package malformed", e);
					}
			    }
		    };
		    channel.basicConsume(queueName, true, consumer);
		    
	    } catch (IOException e) {
	    	logger.log(Level.SEVERE, "Reception failed", e);
	    } catch (TimeoutException e) {
	    	logger.log(Level.SEVERE, "Reception timed out", e);
	    }
	}
	
}
