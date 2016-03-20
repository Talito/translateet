package org.netcomputing.webservices.queues.q4user;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class User {

	private class ProxyLikeString {
		
		private String content = "";
		private boolean initialized = false;
		
		public synchronized void intialize(String conts) {
			content = conts;
			initialized = true;
			notifyAll();
		}
		
		public synchronized String getContent() throws InterruptedException {
			if(!initialized);
				wait();
			return content;
		}
		
	}
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private String host = "localhost";
	private String uid;
	
	public User(String id) {
		uid = id;
	}
	
	public ProxyLikeString getTranslation() {
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(host);
	    ProxyLikeString pls = new ProxyLikeString();
	    try {
	        Connection connection = factory.newConnection();
	        Channel channel = connection.createChannel();

	        channel.queueDeclare(uid, true, false, false, null);
	        
	        logger.log(Level.INFO, "Waiting for my translation...");
	        
		    Consumer consumer = new DefaultConsumer(channel) {
			    @Override
			    public void handleDelivery(String consumerTag, Envelope envelope,
			                               AMQP.BasicProperties properties, byte[] body) throws IOException {
				    String message = new String(body, "UTF-8");
				    logger.log(Level.INFO, "Received message:" + message);
				    pls.intialize(message);
			    }
		    };
		    channel.basicConsume(uid, true, consumer);
		    
	    } catch (IOException e) {
	    	logger.log(Level.SEVERE, "Reception failed", e);
	    } catch (TimeoutException e) {
	    	logger.log(Level.SEVERE, "Reception timed out", e);
	    }
	    return pls;
	}
	
	//TODO: -> testing
	public static void main(String...args) throws InterruptedException {
		String langIdent = "sampleText";
		String usrIdent = "arbitrary";
		String transStr = "Example";
			
		new Thread(new Runnable() {
			public void run() {
				Translator tr = new Translator(langIdent);
				tr.translate();
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				Language lang = Language.getLanguage(langIdent);
				lang.requestTranslation(usrIdent, transStr);	
			}
		}).start();
		
		User us = new User(usrIdent);
		ProxyLikeString pls = us.getTranslation();
		
		System.out.println(pls.getContent());
	}
	
}
