package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author Stijn
 *
 * A very simple controller for the chatter
 */
public class ChatterController {

	private Chatter ch;
	
	public ChatterController(Chatter cha) {
		ch = cha;
	}
	
	public void control() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String ln;
		try {
			ln = br.readLine();
			while(!ln.equals("exit")) {
				ch.talk(ln);
				ln = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			System.err.println("FFS");
		}
	}

	
	
}
