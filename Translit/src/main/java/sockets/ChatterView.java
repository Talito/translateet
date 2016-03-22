package sockets;

import java.util.Observable;
import java.util.Observer;

/**
 * 
 * @author Stijn
 * 
 * A very simple view of a chatter
 */
public class ChatterView implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		System.out.println((Chatter) o);
	}
	
}
