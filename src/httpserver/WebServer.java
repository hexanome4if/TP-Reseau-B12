/**
 * WebServer
 * Web server using HTTP protocol
 * Date: 17/10/2020
 * Authors: Paul Moine and Fabien Narboux
 */

package httpserver;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

  /**
  * Start the server
  **/
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
  
  /**
  * Laucnh the program (create the WebServer and start it)
  **/
  public static void main(String[] args) {
    WebServer ws = new WebServer();
    ws.start();
  }
}
