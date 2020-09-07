package srcCode;

import java.lang.NullPointerException;
import java.sql.*;


public class LoginHandler {
	
	String body = null;
	String username = null;
	String password = null;
	String account_kind = null;
	DatabaseObject dtb = null;
	boolean allParametersGiven = true;


	public LoginHandler(String contentBody, DatabaseObject db) throws NullPointerException {

		body = contentBody;
		dtb = db;

	}

	//Splitting the parameters line and getting the 3 values
	public void extractParameters() {
		String[] parameters = body.split("&");



		//If there is a value given for every parameter, we extract them
		String[] huh = parameters[0].split("=");
		if(huh.length>1) {
			username = huh[1];
		}else{allParametersGiven=false;}	

		String[] hoh = parameters[1].split("=");
		if(hoh.length>1) {
			password = hoh[1];
		}else{allParametersGiven=false;}

	}

	//Checking if the values given are in the database
	public boolean loginCheck() {
		if(allParametersGiven==true) {
			return dtb.checkLoginDetais(username,password);
		}
		return false;
	}


	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	} 

	/*public String getAccountKind() {
		return account_kind;
	} */
}