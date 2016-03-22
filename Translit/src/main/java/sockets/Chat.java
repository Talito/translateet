package sockets;

import java.util.Observable;
import java.util.Observer;

/**
 * 
 * @author Stijn and Jose
 * Test class for the server functionality
 */
public class Chat implements Observer {
	
	public static void main(String...args) throws InterruptedException {
		ServiceServer ss = new ServiceServer(50000);
		new Thread(ss).start();
		ss.addObserver(new Chat());
		Thread.sleep(60000);
		ss.softStop();
	}

	@Override
	public void update(Observable o, Object arg) {
		ServiceServer ss = (ServiceServer) o;
		Chatter ch = ss.sessions.get(0);
		System.out.println(ch);
		ch.addObserver(new ChatterView());
		new ChatterController(ch).control();
		ss.softStop();
	}
	
}
