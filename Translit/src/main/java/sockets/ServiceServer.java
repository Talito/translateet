package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Stijn and Jose
 *
 * TCP server for one to one support chat
 */
public class ServiceServer extends SoftStopThread {
	
	private ServerSocket ss;
	public List<Chatter> sessions;
	private int port;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	/**
	 * Creates a new ServiceServer on port
	 * @param portNo
	 */
	public ServiceServer(int portNo) {
		port = portNo;
		sessions = new ArrayList<>();
	}

	/**
	 * Initializes the serversocket to the port.
	 */
	@Override
	protected boolean initialize() {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not create ServerSocket.", e);
			return false;
		}
		logger.log(Level.INFO, "initialized");
		return true;
	}

	/**
	 * Waits for a connection, and when one arrives, creates a new session
	 * with the incoming user. Support staff can receive a notification
	 */
	@Override
	protected boolean execute() {
		try {
			logger.log(Level.INFO, "Waiting for connection");
			Socket con = ss.accept();
			logger.log(Level.INFO, "Created connection on port " + con.getPort());
			Chatter ch = new Chatter(con);
			sessions.add(ch);
			new Thread(ch).start();
			setChanged();
			notifyObservers();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not accept connection", e);
			return false;
		}
		return true;
	}

	/**
	 * Cleans up the system after termination is initialised.
	 */
	@Override
	protected void cleanUp() {
		if(ss == null)
			return;
		try {
			ss.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unexpected error while shutting down server", e);
		}
	}

}
