package stream.server;

import stream.core.GlobalMessage;
import stream.core.infos.*;
import stream.core.requests.*;
import sun.applet.Main;

import javax.xml.soap.Text;
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
        System.out.println("Received message: " + message.getType());
        switch (message.getType()) {
            case "join-room": {
                JoinRoomRequest joinRoomRequest = (JoinRoomRequest)message.getData();
                ClientData clientData = ClientContainer.getClient(clientSocket);

                if (clientData != null) {
                    List<String> rooms = clientData.getJoinedRooms();
                    if (!rooms.contains(joinRoomRequest.getRoomName())) {
                        clientData.joinRoom(joinRoomRequest.getRoomName());
                        GlobalMessage computedMessage = new GlobalMessage("room-joined", new UserJoinedRoomInfo(pseudo, joinRoomRequest.getRoomName()));
                        computedMessage.setDate();
                        MainServer.broadcastMessage(computedMessage, clientSocket);
                        MainServer.saveInHistory(computedMessage);
                    }
                }
                break;
            }
            case "leave-room": {
                LeaveRoomRequest leaveRoomRequest = (LeaveRoomRequest)message.getData();
                ClientData clientData = ClientContainer.getClient(clientSocket);

                if (clientData != null) {
                    List<String> rooms = clientData.getJoinedRooms();
                    if (rooms.contains(leaveRoomRequest.getRoomName())) {
                        clientData.leaveRoom(leaveRoomRequest.getRoomName());
                        GlobalMessage computedMessage = new GlobalMessage("room-left", new UserLeftRoomInfo(pseudo, leaveRoomRequest.getRoomName()));
                        computedMessage.setDate();
                        MainServer.broadcastMessage(computedMessage, clientSocket);
                        MainServer.saveInHistory(computedMessage);
                    }
                }
                break;
            }
            case "create-room": {
                CreateRoomRequest createRoomRequest = (CreateRoomRequest)message.getData();
                if (RoomManager.addRoom(createRoomRequest.getRoomName())) {
                    GlobalMessage computedMessage = new GlobalMessage("room-new", new NewRoomInfo(createRoomRequest.getRoomName()));
                    MainServer.broadcastMessage(computedMessage, null);
                }

                break;
            }
            case "message": {
                MessageRequest messageRequest = (MessageRequest)message.getData();
                switch(messageRequest.getType()) {
                    case "text": {
                        TextMessageRequest textMessageRequest = (TextMessageRequest)messageRequest.getData();
                        GlobalMessage computedMessage = new GlobalMessage("message", new MessageInfo("text", new TextMessageInfo(textMessageRequest.getMessage()), pseudo, messageRequest.getRoomName()));
                        computedMessage.setDate();
                        MainServer.broadcastMessage(computedMessage, clientSocket);
                        MainServer.saveInHistory(computedMessage);
                        break;
                    }
                    case "file": {
                        FileMessageRequest fileMessageRequest = (FileMessageRequest)messageRequest.getData();
                        GlobalMessage computedMessage = new GlobalMessage("message", new MessageInfo("message", new FileMessageInfo(fileMessageRequest.getFileName(), fileMessageRequest.getFileData()), pseudo, messageRequest.getRoomName()));
                        computedMessage.setDate();
                        MainServer.broadcastMessage(computedMessage, clientSocket);
                        MainServer.saveInHistory(computedMessage);
                        break;
                    }
                }
                break;
            }
            case "disconnect": {
                disconnect();
                break;
            }
            case "connect": {
                ClientData clientData = ClientContainer.getClient(clientSocket);
                if (clientData != null) {
                    ConnectRequest connectRequest = (ConnectRequest)message.getData();
                    clientData.setPseudo(connectRequest.getPseudo());
                    pseudo = connectRequest.getPseudo();
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
                GlobalMessage computedMessage = new GlobalMessage("room-left", new UserLeftRoomInfo(pseudo, room));
                computedMessage.setDate();
                MainServer.broadcastMessage(computedMessage, clientSocket);
                MainServer.saveInHistory(computedMessage);
            }
        }
        ClientContainer.removeClient(clientSocket);
    }

}
