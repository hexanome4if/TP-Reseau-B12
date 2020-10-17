package stream.server;

import stream.core.GlobalMessage;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author polo
 */
public class ClientThread extends Thread {
    /**
     * Current client socket
     */
    private final Socket clientSocket;

    /**
     * Infinite receive loop handler
     */
    private boolean stopLoop = false;

    /**
     * Current client pseudo
     */
    private String pseudo = "";

    /**
     * Create a new thread to handle a specific client identified by it's socket by listening to it's messages
     * @param clientSocket the client socket
     */
    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        // Add the client to the client container
        ClientContainer.addClient(clientSocket);
    }

    /**
     * Receive messages from the clients and handle them
     */
    @Override
    public void run() {
        try {
            ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
            while (!stopLoop) {
                try {
                    GlobalMessage message = (GlobalMessage) reader.readObject();
                    handleMessage(message);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } catch (EOFException ex) {
          try {
            disconnect();
          } catch (IOException e) {
              Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
          }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handle and treat a new message from the client
     * @param message the message sent by the client
     */
    private void handleMessage(GlobalMessage message) throws IOException {
        switch (message.getType()) {
            case "message": {
                GlobalMessage computedMessage = new GlobalMessage(pseudo,"message",message.getData());
                computedMessage.setDate();
                MainServer.broadcastMessage(computedMessage, clientSocket);
                break;
            }
            case "disconnect": {
                disconnect();
                break;
            }
            case "connect": {
              ClientContainer.getClient(clientSocket).setPseudo(message.getPseudo());
              pseudo = message.getPseudo();
              message.setDate();
              MainServer.broadcastMessage(message, clientSocket);
              break;
            }
        }
    }

    /**
     * Method to handle client disconnection (stopping thread, remove client from connected clients list, send disconnection message to everyone)
     */
    private void disconnect() throws IOException {
      stopLoop = true;
      ClientContainer.removeClient(clientSocket);
      GlobalMessage computedMessage = new GlobalMessage(pseudo,"disconnect",null);
      computedMessage.setDate();
      MainServer.broadcastMessage(computedMessage, clientSocket);
    }

}
