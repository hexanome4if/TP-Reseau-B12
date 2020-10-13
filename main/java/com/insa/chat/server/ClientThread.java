package com.insa.chat.server;

import com.insa.chat.core.GlobalMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
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
     * Create a new thread to handle a specific client identified by it's socket by listening to it's messages
     * @param clientSocket the client socket
     */
    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        // Add the client to the client container
        ClientContainer.addClient(clientSocket);
    }

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
            
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handle and treat a new message from the client
     * @param message the message sent by the client
     */
    private void handleMessage(GlobalMessage message) {
        switch (message.getType()) {
            case "message" -> {
                MainServer.broadcastMessage(message, clientSocket);
            }
            case "disconnect" -> {
                // TODO tell other people
                stopLoop = true;
                ClientContainer.removeClient(clientSocket);
            }
        }
    }
    
}
