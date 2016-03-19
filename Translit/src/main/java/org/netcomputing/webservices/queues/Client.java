package org.netcomputing.webservices.queues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

	public static void main(String...args) {
		
		System.out.print("To translate? ");
		System.out.flush();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			//String msg = br.readLine();
			
			
			br.close();
		} catch (IOException e) {
			System.err.println("Could not read from stdin");
		}
		
	}
	
}
