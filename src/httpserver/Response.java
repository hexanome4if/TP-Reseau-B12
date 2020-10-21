/**
 * MainMulticast
 * Multicast UDP client
 * Date: 17/10/2020
 * Authors: Paul Moine and Fabien Narboux
 */

package httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Response {

  /**
  * status code of the response
  **/
  private int statusCode;
  /**
  * HTTP version of the response
  **/
  private String httpVersion;
  /**
  * map of headers of the response
  **/
  private Map<String,String> headers = new HashMap<>();
  /**
  * body (type String) of the response
  **/
  private String body;
  /**
  * body (type byte[]) of the response
  **/
  private byte[] byteBody = null;

  /**
  * Default constructor of the response
  **/
  public Response() {
    body = "";
    statusCode = 200;
    httpVersion = "HTTP/1.0";
    headers.put("Date", new Date().toString());
    headers.put("Server", "Serv'if");
    headers.put("Connection", "Closed");
    headers.put("Content-Type", "text/plain");
  }

  /**
  * Execute the response, it sends data to the client
  * @param outputStream outputStream in which data will be sent
  **/
  public void execute(OutputStream outputStream) {
    try {
      outputStream.write((httpVersion + " " + getStatus() + "\r\n").getBytes());
      for(Map.Entry<String, String> header : headers.entrySet()) {
        outputStream.write((header.getKey() + ": " + header.getValue() + "\r\n").getBytes());
      }

      if(byteBody != null) {
        try {
          outputStream.write(("Content-Length: " + byteBody.length + "\r\n").getBytes());
          // out.println("Last-Modified: TODO");

          // this blank line signals the end of the headers
          outputStream.write(("" + "\r\n").getBytes());
          outputStream.write(byteBody);
        } catch (IOException e) {
          // Do nothing
        }
      } else {
        outputStream.write(("Content-Length: " + body.length() + "\r\n").getBytes());
        // out.println("Last-Modified: TODO");

        // this blank line signals the end of the headers
        outputStream.write(("" + "\r\n").getBytes());

        // Send the HTML page
        outputStream.write(body.getBytes());
      }

      outputStream.flush();

    } catch (Exception e) {

    }

  }

  /**
  * return statusCode
  * @return statusCode
  **/
  public int getStatusCode() {
    return statusCode;
  }

  /**
  * set statusCode
  * @param statusCode
  **/
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  /**
  * return httpVersion
  * @return httpVersion
  **/
  public String getHttpVersion() {
    return httpVersion;
  }

  /**
  * set httpVersion
  * @param httpVersion
  **/
  public void setHttpVersion(String httpVersion) {
    this.httpVersion = httpVersion;
  }

  /**
  * return headers
  * @return headers
  **/
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
  * set one header in headers
  * @param headerName key
  * @param headerContent value
  **/
  public void setHeader(String headerName, String headerContent) {
    this.headers.put(headerName, headerContent);
  }

  /**
  * set headers
  * @param headers
  **/
  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
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
  * @param body
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
  * @param byteBody
  **/
  public void setByteBody(byte[] byteBody) {
    this.byteBody = byteBody;
  }

  /**
  * return status String according to statusCode
  * @return statusCode
  **/
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
