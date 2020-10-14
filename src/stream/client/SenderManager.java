package stream.client;

import java.io.*;
import java.net.*;
import stream.core.*;

public class SenderManager {

    /**
     * Socket connection to server
     */
    private final Socket echoSocket;
    /**
     * Infinite loop manager
     */
    private boolean run = true;
    /**
     * User input reader
     */
    private BufferedReader stdIn = null;
    /**
     * Server output to send messages
     */
    private ObjectOutputStream socOut = null;

    /**
     * Create a class to handle message sending to the server
     * @param s socket connection to the server
     */
    SenderManager(Socket s) {
      this.echoSocket = s;
    }

    /**
     * Start an infinite loop to send messages to the server
     */
    public void run() {
        try {
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            socOut = new ObjectOutputStream(echoSocket.getOutputStream());
            String line;
            /* User pseudo management */
            System.out.println("Pseudo : ");
            line = stdIn.readLine();
            System.out.println("You are connected with the pseudo : " + line);
            System.out.println("");
            // Send the pseudo to the server
            socOut.writeObject(new GlobalMessage(line, "connect", null));

            // Infinite loop to send messages
            while (run) {
                line = stdIn.readLine();

                // Special treatment if message is "disconnect"
                if (line.equals("disconnect")) {
                    run = false;
                    socOut.writeObject(new GlobalMessage("disconnect", null)); // Send disconnect to the server
                } else {
                    socOut.writeObject(new GlobalMessage("message", line)); // Send the message to the server
                    System.out.println("Me: " + line); //On affiche ce que l'on envoie
                }
            }

        } catch (Exception e) {
            System.err.println("Error in SenderThread:" + e);
            e.printStackTrace();
        }
    }

    /**
     * Call to clear streams and infinite loop when the client disconnect
     * @throws IOException
     */
    public void disconnect() throws IOException {
        stdIn.close();
        socOut.close();
    }

}
