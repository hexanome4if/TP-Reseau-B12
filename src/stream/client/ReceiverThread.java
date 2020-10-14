package stream.client;

import java.io.*;
import java.net.*;
import stream.core.*;

public class ReceiverThread extends Thread {

    /**
     * Socket connection to server
     */
    private final Socket echoSocket;
    /**
     * Infinite loop manager
     */
    private boolean run = true;
    /**
     * Input stream from server
     */
    private ObjectInputStream socIn = null;

    /**
     * Create a thread to receive messages from the server
     * @param s server socket connection
     */
    ReceiverThread(Socket s) {
        this.echoSocket = s;
    }

    @Override
    public void run() {
        try {
            socIn = new ObjectInputStream(echoSocket.getInputStream());
            // Infinite loop to receive messages
            while (run) {
                // Get the GlobalMessage
                GlobalMessage message = (GlobalMessage) socIn.readObject();
                // Handle the message
                handleMessage(message);
            }
        } catch (SocketException se) {
            // Ignore exception
        } catch (Exception e) {
            System.err.println("Error in ReceiverThread:" + e);
            e.printStackTrace();
        }
    }

    /**
     * Method used when the client disconnect from the server to clear stream and stop infinite loop
     * @throws IOException
     */
    public void disconnect() throws IOException {
        run = false;
        socIn.close();
    }

    /**
    * Handle and treat a new message from the server
    * @param message the message sent by the server
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
