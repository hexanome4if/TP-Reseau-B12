package stream.server;

import stream.core.GlobalMessage;
import stream.core.infos.NewRoomInfo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author polo
 */
public class MainServer {
    /**
     * History file name in which we will save the chat history
     */
    private static final String historySerializationFileName = "history.ser";
    /**
     * History of sent messages
     */
    private static final List<GlobalMessage> history = new ArrayList<>();
    /**
     * Lock to handle concurrency on the clients socket
     */
    private static final Semaphore socketLock = new Semaphore(1);
    /**
     * Output stream to save the history in a file
     */
    private static ObjectOutputStream oos = null;

    /**
     * Start a TCP server on the port specified as the first CLI argument
     * @param args CLI arguments
     * @throws Exception if an error occurred while creating the server
     */
    public static void main(String[] args) throws Exception {
        // Check CLI arguments
        if (args.length != 1) {
            System.out.println("Usage: java MainServer <MainServer port>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);

        //On lit l'historique des messages
        try {
            FileInputStream fis = new FileInputStream(historySerializationFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);

            while (true) {
                try {
                    history.add((GlobalMessage) ois.readObject());
                } catch (EOFException ex) {
                    break;
                }

            }
        } catch (EOFException e) {
            // Do nothing
        }


        // Find saved rooms
        RoomManager.init();

        // Start the TCP server
        startServer(port);
    }

    /**
     * Start a TCP server on the given port
     *
     * @param port the port the server should listen to
     * @throws IOException if an error occurred while restoring the history
     */
    private static void startServer(int port) throws IOException {

        FileOutputStream fos = new FileOutputStream(historySerializationFileName);
        oos = new ObjectOutputStream(fos);

        for (GlobalMessage gm : history) {
            oos.writeObject(gm); //on réécrit les anciens messages
        }

        ServerSocket listenSocket;

        try {
            listenSocket = new ServerSocket(port); //port
            System.out.println("Server ready...");

            // Loop to accept new clients
            while (true) {

                Socket clientSocket = listenSocket.accept();
                System.out.println("Connexion from:" + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                // Handle the new client in a separate thread
                ClientThread ct = new ClientThread(clientSocket);
                ct.start();
            }
        } catch (Exception e) {
            System.err.println("Error in MainServer:" + e);
        }
    }

    /**
     * Broadcast a message to every connected client except the one identified by socket
     *
     * @param message message to send
     * @param socket  client socket to ignore during broadcast
     */
    public static void broadcastMessage(GlobalMessage message, Socket socket) {
        try {
            socketLock.acquire();
            // Get every connected clients
            Collection<ClientData> clients = ClientContainer.getClients();


            /*if(clients.size() <= 1 && !message.getPseudo().equals("")) //Si c'est le premier client connecté ou le dernier à se déconnecter
            {
              if (message.getType().equals("connect")) {
                history.add(message);
                oos.writeObject(message);
                oos.flush();
              } else if (message.getType().equals("disconnect") && clients.size() == 0) {
                history.add(message);
                oos.writeObject(message);
                oos.flush();
              }
            }*/
            for (ClientData client : clients) {
                // Check if the client is not the one which should be ignored
                if (client.getSocket() != socket && client.getPseudo() != null) {
                    try {
                        client.getOutputStream().writeObject(message); // Send the message

                    /*switch (message.getType()) {
                        case "message": {
                            GlobalMessage gm = new GlobalMessage(message.getPseudo(),"message",message.getData());
                            history.add(gm);
                            oos.writeObject(gm);
                            break;
                        }
                        case "disconnect": {
                            GlobalMessage gm = new GlobalMessage(message.getPseudo(),"message","left the chat");
                            history.add(gm);
                            oos.writeObject(gm);
                            break;
                        }
                        case "connect": {
                            GlobalMessage gm = new GlobalMessage(message.getPseudo(),"message","join the chat");
                            history.add(gm);
                            oos.writeObject(gm);
                            break;
                        }
                    }*/
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            socketLock.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send the history to a client
     * @param socket the client to send the history to
     */
    public static void sendHistory(Socket socket) {
        ClientData client = ClientContainer.getClient(socket);
        if (client == null) return;
        try {
            socketLock.acquire();
            for (GlobalMessage gm : history) {
                try {
                    client.getOutputStream().writeObject(gm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socketLock.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send every rooms to a client
     * @param socket the client to send the rooms to
     */
    public static void sendRooms(Socket socket) {
        ClientData client = ClientContainer.getClient(socket);
        if (client == null) return;
        try {
            socketLock.acquire();
            try {
                RoomManager.lockRooms();
                for(String room : RoomManager.rooms) {
                    try {
                        client.getOutputStream().writeObject(new GlobalMessage("room-new", new NewRoomInfo(room)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                RoomManager.releaseRooms();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            socketLock.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save a message in the history
     * @param message the message to save
     */
    public static void saveInHistory(GlobalMessage message) {
        // Add the message in the history
        try {
            history.add(message);
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
