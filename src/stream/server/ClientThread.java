package stream.server;

import stream.core.GlobalMessage;

import java.io.*;
import java.net.*;
import java.util.List;
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
            case "join-room": {
                String roomName = (String)message.getData();
                ClientData clientData = ClientContainer.getClient(clientSocket);

                if (clientData != null) {
                    List<String> rooms = clientData.getJoinedRooms();
                    if (!rooms.contains(roomName)) {
                        clientData.joinRoom(roomName);
                        GlobalMessage computedMessage = new GlobalMessage(pseudo, "join-room", null, roomName);
                        computedMessage.setDate();
                        MainServer.broadcastMessage(computedMessage, clientSocket);
                        MainServer.saveInHistory(computedMessage);
                    }
                }
                break;
            }
            case "leave-room": {
                String roomName = (String)message.getData();
                ClientData clientData = ClientContainer.getClient(clientSocket);

                if (clientData != null) {
                    List<String> rooms = clientData.getJoinedRooms();
                    if (rooms.contains(roomName)) {
                        clientData.leaveRoom(roomName);
                        GlobalMessage computedMessage = new GlobalMessage(pseudo, "leave-room", null, roomName);
                        computedMessage.setDate();
                        MainServer.broadcastMessage(computedMessage, clientSocket);
                        MainServer.saveInHistory(computedMessage);
                    }
                }
                break;
            }
            case "create-room": {
                if (RoomManager.addRoom((String) message.getData())) {
                    GlobalMessage computedMessage = new GlobalMessage("room", message.getData(), null);
                    MainServer.broadcastMessage(computedMessage, null);
                }

                break;
            }
            case "message-txt": {
                GlobalMessage computedMessage = new GlobalMessage(pseudo,"message-txt",message.getData(), message.getRoomName());
                computedMessage.setDate();
                MainServer.broadcastMessage(computedMessage, clientSocket);
                MainServer.saveInHistory(computedMessage);
                break;
            }
            case "message-file": {
                GlobalMessage computedMessage = new GlobalMessage(pseudo, "message-file", message.getData(), message.getRoomName());
                computedMessage.setDate();
                MainServer.broadcastMessage(computedMessage, clientSocket);
                MainServer.saveInHistory(computedMessage);
            }
            case "disconnect": {
                disconnect();
                break;
            }
            case "connect": {
                System.out.println("Connect");
                ClientData clientData = ClientContainer.getClient(clientSocket);
                if (clientData != null) {
                    System.out.println("Ok");
                    clientData.setPseudo(message.getPseudo());
                    pseudo = message.getPseudo();
                    MainServer.sendHistory(clientSocket);
                    MainServer.sendRooms(clientSocket);
                }
              break;
            }
        }
    }

    /**
     * Method to handle client disconnection (stopping thread, remove client from connected clients list, send disconnection message to everyone)
     */
    private void disconnect() throws IOException {
        stopLoop = true;
        ClientData clientData = ClientContainer.getClient(clientSocket);
        if (clientData != null) {
            // Send a leave message to every joined room
            for(String room: clientData.getJoinedRooms()) {
                GlobalMessage computedMessage = new GlobalMessage(pseudo, "leave-room", null, room);
                computedMessage.setDate();
                MainServer.broadcastMessage(computedMessage, clientSocket);
                MainServer.saveInHistory(computedMessage);
            }
        }
        ClientContainer.removeClient(clientSocket);
    }

}
