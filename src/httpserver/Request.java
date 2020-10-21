/**
 * Request
 * Request that the server will receive
 * Date: 17/10/2020
 * Authors: Paul Moine and Fabien Narboux
 */
package httpserver;

import httpserver.middlewares.AdminMiddleware;
import httpserver.middlewares.ExecMiddleware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Request {

  /**
  * path of the request
  **/
  private String path;
  /**
  * method of the request
  **/
  private final String method;
  /**
  * httpVersion of the request
  **/
  private final String httpVersion;
  /**
  * headers of the request
  **/
  private final Map<String,String> headers = new HashMap<>();
  /**
  * body (in String) of the request
  **/
  private String body;
  /**
  * body (in byte[]) of the request
  **/
  private byte[] byteBody;
  /**
  * params of the path
  **/
  private final Map<String,String> queryParams = new HashMap<>();
  /**
  * string containing params of the path
  **/
  private String queryString;

  /**
  * Constructor of the Request
  * @param headers list of headers of the request, the constructor will parse the list
  */
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

  /**
  * return path
  * @return path
  **/
  public String getPath() {
    return path;
  }

  /**
  * return method
  * @return method
  **/
  public String getMethod() {
    return method;
  }

  /**
  * return httpVersion
  * @return httpVersion
  **/
  public String getHttpVersion() {
    return httpVersion;
  }

  /**
  * return headers
  * @return headers
  **/
  public Map<String,String> getHeaders() {
    return headers;
  }

  /**
  * return a header according to the key
  * @param key name of the header
  * @return value of the header
  **/
  public String getHeader(String key) {
    if(headers.containsKey(key)) {
      return headers.get(key);
    }
    return null;
  }

  /**
  * return body
  * @return body
  **/
  public String getBody() {
    return body;
  }

  /**
  * set body
  * @param body body as string
  **/
  public void setBody(String body) {
    this.body = body;
  }

  /**
  * return byteBody
  * @return byteBody
  **/
  public byte[] getByteBody() {
    return byteBody;
  }

  /**
  * set byteBody
  * @param byteBody the body as an array of bytes
  **/
  public void setByteBody(byte[] byteBody) {
    this.byteBody = byteBody;
  }

  /**
  * return queryString
  * @return queryString
  **/
  public String getQueryString() {
    return queryString;
  }

  /**
   * Get a query parameter identified by it's name
   * @param name name of the query parameter
   * @return the value of the parameter if it exists null if it does not
   */
  public String getQueryParam(String name) {
    if (queryParams.containsKey(name)) {
      return queryParams.get(name);
    }
    return null;
  }

  /**
   * execute the request and then return the response of the Server
   * @return response generated by the server
   **/
  public Response executeRequest() {
    Response response;

    response = new ExecMiddleware().execute(this);
    if (response != null) return response;

    response = new AdminMiddleware().execute(this);
    if (response != null) return response;

    if (path.equals("") || path.equals("/")) {
      path = "index.html";
    }

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
