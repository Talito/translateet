package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Stijn and Jose
 *
 * TCP server for one to one support chat
 */
public class ServiceServer extends Observable implements Runnable {
	
	private ServerSocket ss;
	public List<Chatter> sessions;
	private int port;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	/**
	 * Creates a new ServiceServer on port
	 * @param portNo
	 * @throws IOException if the socket can't be opened
	 */
	public ServiceServer(int portNo) throws IOException {
		port = portNo;
		sessions = new ArrayList<>();
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not create ServerSocket.", e);
			throw e;
		}
		logger.log(Level.INFO, "initialized");
	}

	/**
	 * Waits for a connection, and when one arrives, creates a new session
	 * with the incoming user. Support staff can receive a notification
	 */
	@Override
	public void run() {
		while(true) {
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
			}
		}
	}

	/**
	 * Cleans up the system after thread termination.
	 */
	public void cleanUp() {
		if(ss == null)
			return;
		try {
			ss.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unexpected error while shutting down server", e);
		}
	}

}
