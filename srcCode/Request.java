package srcCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.NullPointerException;
import java.lang.IndexOutOfBoundsException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;  
import java.io.UnsupportedEncodingException;
import java.util.regex.*;


public class Request {
    
    String _type = "";
    String _resource = "";
    String _protocol = "";
    Integer _contentLength = null;
    Socket _clientSocket = null;
    ArrayList<String> _headers = null;
    String _body = "";
    boolean badRequest = false;
    boolean correctLogin = false;
    String accountName = "";
    boolean loggedIn = false;
    boolean isLogin = false;
    String[] loginParameters = null;


    public Request(Socket clientSocket, DatabaseObject db) throws IOException, NullPointerException {
        _headers = new ArrayList<String>();
        _clientSocket = clientSocket;

        try{
            loadInputStream();
        }
        catch(NullPointerException l) {
            System.out.println("NullPointerException!");
            badRequest = true;

        }

        if(isPost()) {
            
             loginParameters = _body.split("=");

             if(loginParameters.length>0) {
                //Checking if this a login post request.
                if(loginParameters[0].equals("name")) {

                    isLogin = true;

                    LoginHandler lh = new LoginHandler(_body, db);
                    lh.extractParameters();

                    accountName = lh.getUsername();

                    correctLogin = lh.loginCheck();

                    //Loads the login page again if the account detais given were wrong
                    if(correctLogin==false) {
                        _resource = "/esxoli.html";
                    }
                    else {
                        loggedIn = true;
                    }
                }
            }
        }
    }

    public boolean isLoginPost() {
        return isLogin;
    }

    public boolean isCorrectLogin() {
        return correctLogin;
    }

    public String getBody() {
        return _body;
    }

    public String getAccountName() {
        return accountName;
    }

    public boolean logCheckForRequestHandler() {
        return loggedIn;
    }

    public String getResource() {
        return _resource;
    }

    //The date the post is made, to be added to the Database
    public String getPostDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
        String strDate = formatter.format(date);  
        return strDate;
    }
 
    public boolean isGet() {
        return (_type != null && _type.equals("GET"));
    }

    public boolean isPost() {
        return (_type != null && _type.equals("POST"));
    }

    //Processing the input of the request sent to the browser
    private void loadInputStream() throws IOException, NullPointerException {
        String line = null;
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(_clientSocket.getInputStream()));


        boolean isFirstLine = true;
        while(true) {
            line = in.readLine();


            if (isFirstLine) {
                parseFirstLine(line);
                isFirstLine = false;
            }

            if (line.startsWith("Content-Length:")) {
                extractContentLength(line);
            }

            //The parameters in a post request follow the headers after an empty line
            if (line.isEmpty()) {
                if (isPost()) {
                    loadPostData(in);
                }
                break;
            }
            _headers.add(line);
        }
    }

    private void loadPostData(BufferedReader in) throws IOException {
        // Here we need to load stuff in as integers
        // and then typecast them to chars
        // Source: https://stackoverflow.com/a/21729100
        //
        // Also keep in mind that this will probably need to
        // be revisited if/when we handle non-text data (i.e. image uploads)
        int c = 0;
        for(int i = 0; i < _contentLength; i++) {
            c = in.read();
            _body += (char) c;
        }
    }

    private void extractContentLength(String line) {
        String[] parts = line.split(":");
        parts[1] = parts[1].trim();
        _contentLength = Integer.parseInt(parts[1]);
    }

    private void parseFirstLine(String line) {
        
        String[] pieces = line.split("\\s");
        _type = pieces[0];            
        _protocol = pieces[2];

        try{
            _resource = URLDecoder.decode(pieces[1], "UTF8");
        }catch(UnsupportedEncodingException b) {
            b.printStackTrace();
        }

        if((_type.compareTo("")==0) || (_resource.compareTo("")==0) || (_resource.compareTo("/")==0) || (_protocol.compareTo("")==0)) {
            badRequest = true;
        }
    }
    
}
