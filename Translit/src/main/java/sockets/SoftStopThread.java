package sockets;

import java.util.Observable;

/**
 * 
 * @author Stijn and Jose
 * attempt at making a reusable interface to make specific kinds of threads
 * less bulky.
 */
abstract public class SoftStopThread extends Observable implements Runnable {
	
	/**
	 * Whether or not this thread should remain active
	 */
	private boolean active;
	
	public SoftStopThread() {
		active = true;
	}
	
	protected synchronized boolean imActive() {
		return active;
	}
	
	/**
	 * Has this thread die on the next iteration
	 */
	public final synchronized void softStop() {
		active = false;
	}
	
	/**
	 * Template method for these threads
	 */
	public void run() {
		if(initialize())
			while(imActive())
				if(!execute())
					break;
		cleanUp();
	}
	/* In our current implementation execute and cleanup are always necessary */
	protected boolean initialize() { return true; };
	
	protected abstract boolean execute();
	
	protected abstract void cleanUp();
}
