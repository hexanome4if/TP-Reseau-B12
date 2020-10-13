/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package stream.client;

import java.io.*;
import java.net.*;
import stream.core.*;



public class SenderThread {

  private Socket echoSocket;
  private boolean run = true;
  private BufferedReader stdIn = null;
  private ObjectOutputStream socOut = null;

  SenderThread(Socket s) {
    this.echoSocket = s;
  }

  public void run() {
    	  try {
      		stdIn = new BufferedReader(new InputStreamReader(System.in));
      		socOut = new ObjectOutputStream(echoSocket.getOutputStream());
          String line;
          System.out.println("Pseudo : ");
          line=stdIn.readLine();
          System.out.println("You are connected with the pseudo : " + line);
          System.out.println("");
          socOut.writeObject(new GlobalMessage(line, "connect",null));
          while (run) {
          	line=stdIn.readLine();
            if(line.equals("disconnect")) {
              run = false;
              socOut.writeObject(new GlobalMessage("disconnect",null));
            } else {
              socOut.writeObject(new GlobalMessage("message",line));
              System.out.println("Me: " + line); //On affiche ce que l'on envoie
            }
          }

      	} catch (Exception e) {
          	System.err.println("Error in SenderThread:" + e);
            e.printStackTrace();
        }
  }

  public void disconnect() throws IOException {
    stdIn.close();
    socOut.close();
  }

}
