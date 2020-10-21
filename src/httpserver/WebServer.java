/**
 * WebServer
 * Web server using HTTP protocol
 * Date: 17/10/2020
 * Authors: Paul Moine and Fabien Narboux
 */

package httpserver;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 */
public class WebServer {

  /**
   * Start the server
   *
   * @param port port in which the server should listen
   **/
  protected void start(int port) {
    ServerSocket s;

    System.out.println("Webserver starting up on port " + port);
    System.out.println("(press ctrl-c to exit)");

    try {
      // create the main server socket
      s = new ServerSocket(port);
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
   * Launch the program (create the WebServer and start it)
   * @param args CLI arguments with the server port
   **/
  public static void main(String[] args) {
    // Check CLI arguments
    if (args.length != 1) {
      System.out.println("Usage: java MainServer <MainServer port>");
      System.exit(1);
    }

    int port = Integer.parseInt(args[0]);

    WebServer ws = new WebServer();
    ws.start(port);
  }
}
