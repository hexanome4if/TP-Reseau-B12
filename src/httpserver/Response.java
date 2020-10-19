package httpserver;

import java.io.PrintWriter;
import java.util.*;

public class Response {

  private int statusCode;
  private String httpVersion;
  private Map<String,String> headers = new HashMap<>();
  private String body;

  public Response() {
    body = "";
    statusCode = 200;
    httpVersion = "HTTP/1.0";
    headers.put("Date", new Date().toString());
    headers.put("Server", "Serv'if");
    headers.put("Connection", "Closed");
    headers.put("Content-Type", "text/plain");
  }

  public void execute(PrintWriter out) {
    out.println(httpVersion + " " + getStatus());
    for(Map.Entry<String, String> header : headers.entrySet()) {
      out.println(header.getKey() + ": " + header.getValue());
    }
    out.println("Content-Length: " + body.length());
    // out.println("Last-Modified: TODO");

    // this blank line signals the end of the headers
    out.println("");
    // Send the HTML page
    out.println(body);
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

  private String getStatus() {
    switch (statusCode) {
      case 200:
        return "200 OK";
      case 201:
        return "201 Created";
      case 202:
        return "202 Accepted";
      case 204:
        return "204 No Content";
      case 400:
        return "400 Bad Request";
      case 401:
        return "401 Unauthorized";
      case 403:
        return "403 Forbidden";
      case 404:
        return "404 Not Found";
      case 405:
        return "405 Method Not Allowed";
      case 500:
        return "500 Internal Server Error";
      case 501:
        return "501 Not Implemented";
      case 503:
        return "503 Service Unavailable";
    }
    return "501 Not Implemented";
  }
}
