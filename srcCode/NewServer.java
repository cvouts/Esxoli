package srcCode;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.*;
import java.io.File;
import java.io.FileOutputStream;


public class NewServer {
	
	static String loggedInUser = "";
	static String resource = "";


	public String getUser() {
		return loggedInUser;
	}


	public String getResource() {
		return resource;
	}


	public static void main(String[] args) throws IOException {


		String root = "html";

		//Starting the server and creating thedatabase
		ServerSocket ss = new ServerSocket(5060);
		DatabaseObject db = new DatabaseObject();
		db.initialCall();

		//Printing site and accounts details
		System.out.println("\nServer started, the site is at localhost:5060/esxoli.html\n");
		db.printAllAccounts();


		while(true) {

			//Accepting the request and creating a request and requesthandler object 
			Socket cs = ss.accept();
			OutputStream out = cs.getOutputStream();
	        
	        db.setUsername(loggedInUser);
	        Request r = new Request(cs, db);

	        resource = r.getResource();

        	if(r.isCorrectLogin()) {

        		String temp = "";
        		temp = r.getAccountName();

	        	if(!temp.equals("")) {
		        	loggedInUser = r.getAccountName(); 	
		        	
	        	}
        	}
        	
	        RequestHandler rqh = new RequestHandler(r, root);

	        //Serving the response
	        byte[] hbytes = rqh.getResponse().toString().getBytes();
	        byte[] cbytes = rqh.getResponse().getContent();
  
	        out.write(ByteBuffer.allocate(hbytes.length + cbytes.length)
	                                        .put(hbytes)
	                                        .put(cbytes)
	                                        .array());                  
	        cs.close();
        }
	}


}