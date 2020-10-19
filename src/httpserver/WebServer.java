package httpserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class WebServer {

  protected void start() {
    ServerSocket s;

    System.out.println("Webserver starting up on port 3000");
    System.out.println("(press ctrl-c to exit)");
    try {
      // create the main server socket
      s = new ServerSocket(3000);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection");
    for (; ; ) {
      try {
        // wait for a connection
        Socket remote = s.accept();
        // remote is now the connected socket
        System.out.println("Connection, sending data.");
        BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));


        // read the data sent. We basically ignore it,
        // stop reading once a blank line is hit. This
        // blank line signals the end of the client HTTP
        // headers.

        String str = ".";
        List<String> headers = new ArrayList<>();

        while (str != null && !str.equals("")) {
          str = in.readLine();
          headers.add(str);
          System.out.println(str);
        }
        Request request = new Request(headers);
        String contentLength = request.getHeader("Content-Length");
        char[] body = null;
        if(contentLength != null) {
          int intContentLength = Integer.parseInt(contentLength);
          body = new char[intContentLength];
          in.read(body, 0, intContentLength);
        }

        System.out.println("Body : ");
        if(body != null) {
          System.out.println(body);
          request.setBody(new String(body));
        }

        // Execute request and send response
        Response response = request.executeRequest();
        response.execute(remote.getOutputStream());

        remote.close();
      } catch (Exception e) {
        System.out.println("Error: " + e);
      }
    }
  }

  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }
}
