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
        new ClientThread(remote).start();
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
