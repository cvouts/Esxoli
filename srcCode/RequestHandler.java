package srcCode;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.lang.NullPointerException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.regex.*;


public class RequestHandler {
    
    String _root = "";
    String _resourcePath = "";
    String _protocol = "";
    String logDate = "";
    Request _request = null;
    int _statusCode = 0;
    byte[] _content = null;
    Response re = new Response();
    File resourceFile = null;
    HtmlCreator htmlc = new HtmlCreator();


    public RequestHandler(Request request, String root) {
        _request = request;
        _protocol = request._protocol;
        _setRoot(root);
        _setResource(request.getResource());

    }

////////// Methods called by the constructor////////////

    private void _setRoot(String root) {
        _root = root;
    }

    private void _setResource(String resource) {
        _resourcePath = _root + resource;
        resourceFile = new File(_resourcePath);
    }

////////// Method called by main///////////////////////
   public Response getResponse() throws IOException {
        _setResponseContent();
        _setResponseHeaders();
        return re;
    }

////////// Methods called by getResponse///////////////

    private void _setResponseContent() throws IOException {
        re.setStatusCode(200);

        re._HTTP_PROTOCOL = _protocol;

        //if the protocol is not acceptable, return status code 400
        if((_protocol.compareTo("HTTP/1.1")!=0) && (_protocol.compareTo("HTTP/1.0")!=0)) {
            re.setStatusCode(400);
        }

        //if the request is not formed correctly, return status code 400
        if(_request.badRequest == true) {
            re.setStatusCode(400);
        }
        _statusCode = re.getStatusCode();

        //if the request method is not GET or POST, return status code 405
        boolean methodCheck = false;
        if((_request.isGet()) || (_request.isPost())) {
            methodCheck = true;
        }
        if(methodCheck==false) {
            re.setStatusCode(405);
        }

        _statusCode = re.getStatusCode();


        //if there is no problem with the request, try to load a file
        if(_statusCode==200) {

            if(_request.isPost()) {

                if(_request.isLoginPost()==false) {
                    htmlc.addPost(_request.getBody(), _request.getPostDate());
                    htmlc.serveMainPage();
                }
                else {
                    htmlc.serveLessons();
                }
            }
            else{

                String ti =  _request.getResource();

                if((ti.equals("/esxoli.html")) || (ti.equals("/Lessons.html")) || (ti.equals("/about.html"))) {
                }
                else {

                    String[] splittingForHtml = ti.split("\\.");
             
                    if(splittingForHtml.length>1) {
                       if(splittingForHtml[1].equals("html")) {

                            htmlc.serveMainPage();
                        }
                    }
                }
            }
        
            try{
                loadFile();
            }catch(IOException e) {
                //if there is no file like the one requested, return 404
                re.setStatusCode(404);
               _statusCode = re.getStatusCode();
            }
        }
        re.setContent(_content);
    }

    private void loadFile() throws IOException {
        _content = Files.readAllBytes(Paths.get(_resourcePath));
    }


////////// Setting Response Headers ///////////////////////
    public void setDateHeader() {
        Date date = new Date();
        String stringDate = String.valueOf(date);
        logDate = stringDate;
        re.setHeader("Date", stringDate);
    }

    public void setServerHeader() {
        re.setHeader("Server", "testServer");
    }

    public void setLastModifiedHeader() throws IOException {
        FileTime fileTime = null;
        Path path = Paths.get(_resourcePath);
         fileTime = Files.getLastModifiedTime(path);
    
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        String lastModifiedDateString = String.valueOf(dateFormat.format(fileTime.toMillis()));
        re.setHeader("Last-Modified",  lastModifiedDateString);
    } 

    public void setContentLengthHeader() {
        String stringContentLength = String.valueOf(resourceFile.length());
        re.setHeader("Content-Length", stringContentLength);
    }

    public void setContentTypeHeader() throws IOException {
        String[] resourceSplitting = _resourcePath.split("\\.");
        String resourceSuffix = "." +resourceSplitting[resourceSplitting.length-1];
    }

    public void setNotKeepAliveHeader() {
        re.setHeader("Connection", "close");
    }

    private void _setResponseHeaders() throws IOException {
        setDateHeader();
        setServerHeader();
        if(_statusCode == 200) {
            setContentTypeHeader();
            setContentLengthHeader();
            setNotKeepAliveHeader();
        }
    }
}

