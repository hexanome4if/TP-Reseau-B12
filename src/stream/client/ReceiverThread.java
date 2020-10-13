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



public class ReceiverThread extends Thread {

  private Socket echoSocket;
  private boolean run = true;
  private ObjectInputStream socIn = null;

  ReceiverThread(Socket s) {
    this.echoSocket = s;
  }

  public void run() {
        try {
          socIn = new ObjectInputStream(echoSocket.getInputStream());
          while (run) {
            GlobalMessage message = (GlobalMessage) socIn.readObject(); //TODO : afficher la ligne reçu sur l'interface du client
            handleMessage(message);
          }
        } catch (SocketException se) {

        } catch (Exception e) {
            System.err.println("Error in ReceiverThread:" + e);
            e.printStackTrace();
        }
  }

  public void disconnect() throws IOException {
    run = false;
    socIn.close();
  }

  /**
   * Handle and treat a new message from the client
   * @param message the message sent by the client
   */
  private void handleMessage(GlobalMessage message) {
      switch (message.getType()) {
          case "message": {
              System.out.println(message.getPseudo() + " : " + message.getData());
              break;
          }
          case "disconnect": {
              System.out.println(message.getPseudo() + " : left the chat");
              break;
          }
          case "connect": {
            System.out.println(message.getPseudo() + " : join the chat");
            break;
          }
      }
  }

}
