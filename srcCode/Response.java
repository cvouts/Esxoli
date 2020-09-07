package srcCode;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class Response {
    String _HTTP_PROTOCOL = "";

    HashMap<String, String> _headers = new HashMap<>();
    byte[] _content;
    int _statusCode = 0;
   //int isLoggedIn = 0;

    public void setHeader(String headerName, String value) {
        _headers.put(headerName, value);
    }

    public void setContent(byte[] content) {

        _content = content;
        if(_statusCode == 404) {
            _content = "404 File not found".getBytes();
        }
        else if(_statusCode == 405) {
            _content = "405 Method not allowed".getBytes();
        }
        else if(_statusCode ==400) {
            _content = "400 Bad request".getBytes();
        }
    
    }

    public void setStatusCode(int statusCode) {
        _statusCode = statusCode;
    }

    public String toString() {
        return _getStatusLine() + _getHeaders() + "\r\n";
    }

    private String _getHeaders() {
        String headers = "";
        for (Map.Entry<String, String> entry : _headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            headers = headers + key + ": " + value + "\r\n";
        }
        return headers;
    }

    public byte[] getContent() {
        return _content;
    }

    public int getStatusCode() {
        return _statusCode;
    }

    private String _getStatusLine() {
        return _HTTP_PROTOCOL + " " + _statusCode + "\r\n";
    }

    //Use this wherever you need to check if this is a logged in session
    public String getCookie() {

        return _headers.get("Cookie");

    }
 
}
