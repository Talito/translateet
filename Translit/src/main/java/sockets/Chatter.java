package sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Stijn and Jose
 * Represents a one-to-one chat agent
 */
public class Chatter extends SoftStopThread {

	private Logger logger;
	private BufferedReader in;
	private BufferedWriter out;
	private List<String> ll;
	
	/**
	 * Initialises the default fields of the Chatter
	 */
	private Chatter() {
		logger = Logger.getLogger(this.getClass().getName());
		ll = Collections.synchronizedList(new LinkedList<>());
	}
	
	/**
	 * Creates a Chatter using only the provided socket for in and
	 * output
	 * @param socket
	 */
	public Chatter(Socket socket) {
		this();
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not open connections", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Connects to the
	 * @param ip
	 * and
	 * @param portNo
	 * specified for chatting.
	 */
	public Chatter(String ip, int portNo) {
		this();
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket(ip, portNo);
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not open connections", e);
		}
	}
	
	/**
	 * Method for sending a message to the connected chatter, or perhaps chatbox.
	 * @param text
	 * is put into the list of previous messages
	 */
	public void talk(String text) {
		try {
			out.write(text.endsWith(System.lineSeparator()) ? text : text + System.lineSeparator());
			out.flush();
			ll.add(text);
			setChanged();
			notifyObservers();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not send message", e);
			ll.add("Failed to send " + '"' + text + '"');
		}
	}

	/**
	 * If it can, receives a chat message and stores it in
	 * a list. To be able to stop the thread gracefully, it
	 * does not block on read.
	 */
	@Override
	protected boolean execute() {
		try {
			if(!in.ready())
				return true;
			ll.add(in.readLine());
			setChanged();
			notifyObservers();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not receive user request", e);
			return false;
		}
		return true;
	}
	
	/**
	 * Closes the connections when the thread finishes.This should
	 * not be used afterwards.
	 */
	@Override
	protected void cleanUp() {
		try {
			logger.log(Level.INFO, "Closing connections");
			in.close();
			out.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error when closing input", e);
		}
	}
	
	/**
	 * Gives a list of all messages said in this session.
	 */
	public String toString() {
		String ret = new String();
		synchronized (ll) {
			for(String str : ll) {
				ret += str + System.lineSeparator();
			}
		}
		return ret;
	}
	
}
