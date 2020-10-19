package httpserver;

import java.util.*;

public class Request {

  private String path;
  private String method;
  private String httpVersion;
  private Map<String,String> headers = new HashMap<>();
  private String body;

  public Request(List<String> headers) {
    String firstLine = headers.get(0);
    method = firstLine.split(" ")[0];
    path = firstLine.split(" ")[1];
    httpVersion = firstLine.split(" ")[2];
    headers.remove(0);
    for (String s : headers) {
      if(s.split(": ", 2).length < 2){
        break;
      }
      this.headers.put(s.split(": ", 2)[0], s.split(": ")[1]);
    }
  }

  public String getPath() {
    return path;
  }

  public String getMethod() {
    return method;
  }

  public String getHttpVersion() {
    return httpVersion;
  }

  public Map<String,String> getHeaders() {
    return headers;
  }

  public String getHeader(String key) {
    if(headers.containsKey(key)) {
      return headers.get(key);
    }
    return null;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Response executeRequest() {
    AbstractHandle handler;
    switch (method) {
      case "POST":
        handler = new HandlePost();
        break;
      case "GET":
      default:
        handler = new HandleGet();
    }
    return handler.execute(this);
  }

}
