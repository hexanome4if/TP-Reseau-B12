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
        PrintWriter out = new PrintWriter(remote.getOutputStream());

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
        }

        // Send the response
        // Send the headers
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
