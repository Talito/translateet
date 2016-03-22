package sockets;

/**
 * 
 * @author Stijn and Jose
 * Test class for a distant Chatter.
 */

public class Chat2 {

	public static void main(String...args) throws InterruptedException {
		Chatter ch = new Chatter("localhost", 50000);
		ch.addObserver(new ChatterView());
		new Thread(ch).start();
		new ChatterController(ch).control();
	}
	
}
