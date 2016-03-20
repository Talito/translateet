package org.netcomputing.webservices.queues.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.util.logging.Logger;

public class Sim {
	
	private static String host = "localhost";
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
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
	
	public void requestTranslation(String uid, String lang, String text) {
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(host);
	    
	    try {
		    Connection connection = factory.newConnection();
		    
		    Channel channel = connection.createChannel();
		    channel.exchangeDeclare(lang, "fanout");
		    String qName = channel.queueDeclare().getQueue();
		    channel.queueBind(qName, lang, "");
		    
		    StringBuilder sb = new StringBuilder();
		    sb.append("User(l=").append(uid.length()).append(")=").append(uid);
		    sb.append("Message(l=").append(text.length()).append(")=").append(text);
		    
		    channel.basicPublish(lang, "", null, sb.toString().getBytes("UTF-8"));
		    
		    logger.log(Level.INFO, "Enqueued <" + text + "> for <" + uid + ">");
		    
		    channel.close();
		    connection.close();
	    } catch (IOException e) {
	    	logger.log(Level.SEVERE, "Sending <" + text + "> for <" + uid
	    			+ "> failed.", e);
	    } catch (TimeoutException e) {
	    	logger.log(Level.SEVERE, "Sending <" + text + "> for <" + uid
	    			+ "> timed out.", e);
	    }
		
	}
	
	public ProxyLikeString getTranslation(String lang) {
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(host);
	    ProxyLikeString pls = new ProxyLikeString();
	    try {
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
	
		    channel.exchangeDeclare(lang, "fanout");
		    String queueName = channel.queueDeclare().getQueue();
		    channel.queueBind(queueName, lang, "");
	
		    logger.log(Level.INFO, " [*] Waiting for messages. To exit press CTRL+C");
	
		    Consumer consumer = new DefaultConsumer(channel) {
		      @Override
		      public void handleDelivery(String consumerTag, Envelope envelope,
		                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
		        String message = new String(body, "UTF-8");
		        //System.out.println(" [x] Received '" + message + "'");
		        pls.intialize(message);
		      }
		    };
		    channel.basicConsume(queueName, true, consumer);
		    
	    } catch (IOException e) {
	    	logger.log(Level.SEVERE, "Reception failed", e);
	    } catch (TimeoutException e) {
	    	logger.log(Level.SEVERE, "Reception timed out", e);
	    }
	    return pls;
	}
	
	public static void main(String...args) throws InterruptedException {
		String lang = "a";
		new Thread(new Runnable(){
			public void run() {
				try {
					String msg = new Sim().getTranslation(lang).getContent();
					Logger.getLogger(this.getClass().getName()).log(Level.INFO, msg + " 1 "); 
				} catch (InterruptedException e) {
					Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not get translation");
				}
			}
		}).start();
		new Thread(new Runnable(){
			public void run() {
				try {
					String msg = new Sim().getTranslation(lang).getContent();
					Logger.getLogger(this.getClass().getName()).log(Level.INFO, msg + " 2 "); 
				} catch (InterruptedException e) {
					Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not get translation");
				}
			}
		}).start();
		new Thread(new Runnable(){
			public void run() {
				try {
					String msg = new Sim().getTranslation(lang).getContent();
					Logger.getLogger(this.getClass().getName()).log(Level.INFO, msg + " 3 "); 
				} catch (InterruptedException e) {
					Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not get translation");
				}
			}
		}).start();
		Thread.sleep(500);
		new Thread(new Runnable(){
			public void run() {
				for(int i = 0; i < 3; ++i)
					new Sim().requestTranslation("Stijn", lang, "Productivity");
			}
		}).start();
	}
}
