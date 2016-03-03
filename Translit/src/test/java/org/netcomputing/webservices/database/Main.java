package org.netcomputing.webservices.database;

import java.io.IOException;

import org.netcomputing.webservices.datamodel.User;
import org.netcomputing.webservices.server.ConfigLoader;

public class Main {
	
	public static void main(String args[]) throws IOException {
		User u1 = new User(); u1.setUID(001); u1.setName("Jasel"); u1.setScore(7);
		User u2 = new User(); u2.setUID(002); u2.setName("Timmy"); u2.setScore(5);		
		TestDAO tdao = new TestDAO();
		User u3 = new User();
		u3 = tdao.getUser("001");
		User u4 = new User();
		u4 = tdao.getUser("002");
		System.out.println(u3 + " and " + u4);
	}
}
