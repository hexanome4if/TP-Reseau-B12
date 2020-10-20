package httpserver;

import java.util.*;

public class Request {

  private String path;
  private final String method;
  private final String httpVersion;
  private final Map<String,String> headers = new HashMap<>();
  private String body;
  private Map<String,String> queryParams = new HashMap<>();
  private String queryString;

  public Request(List<String> headers) {
    String firstLine = headers.get(0);
    method = firstLine.split(" ")[0];
    path = firstLine.split(" ")[1];
    if (path.split("\\?").length > 1) {
      queryString = "?" + path.split("\\?", 2)[1];
      String[] params = path.split("\\?", 2)[1].split("&");
      path = path.split("\\?")[0];
      for (String p : params) {
        String key = p.split("=", 2)[0];
        String value = "";
        if (p.split("=", 2).length > 1) {
          value = p.split("=", 2)[1];
        }
        queryParams.put(key, value);
      }
    }
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

  public String getQueryString() {
    return queryString;
  }

  public Response executeRequest() {
    AbstractHandle handler;
    switch (method) {
      case "POST":
        handler = new HandlePost();
        break;
      case "DELETE":
        handler = new HandleDelete();
        break;
      case "HEAD":
        handler = new HandleHead();
        break;
      case "PUT":
        handler = new HandlePut();
        break;
      case "GET":
      default:
        handler = new HandleGet();
    }
    return handler.execute(this);
  }

}
