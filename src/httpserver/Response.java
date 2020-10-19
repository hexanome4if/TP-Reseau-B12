package httpserver;

import java.io.PrintWriter;
import java.util.*;

public class Response {

  private int statusCode;
  private String httpVersion;
  private Map<String,String> headers = new HashMap<>();
  private String body;

  public void execute(PrintWriter out) {
    out.println("HTTP/1.0 200 OK");
    out.println("Date: TODO");
    out.println("Content-Type: text/html");
    out.println("Server: Bot");
    out.println("Last-Modified: TODO");
    out.println("Connection: Closed");
    // this blank line signals the end of the headers
    out.println("");
    // Send the HTML page
    out.println("<H1>Welcome to the Ultra Mini-WebServer</H1>");
    out.flush();
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getHttpVersion() {
    return httpVersion;
  }

  public void setHttpVersion(String httpVersion) {
    this.httpVersion = httpVersion;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeader(String headerName, String headerContent) {
    this.headers.put(headerName, headerContent);
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
