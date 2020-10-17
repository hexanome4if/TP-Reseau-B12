package stream.server;

import stream.core.GlobalMessage;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author polo
 */
public class MainServer {

    private static final List<GlobalMessage> history = new ArrayList<>();
    private static ObjectOutputStream oos = null;
    private static FileOutputStream fos = null;
    private static ObjectInputStream ois = null;
    private static FileInputStream fis = null;
    private static final String nomFichierSerialization = "history.ser";

    /**
     * Start a TCP server on the port specified as the first CLI argument
     *
     * @param args CLI arguments
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
            fis = new FileInputStream(nomFichierSerialization);
            ois = new ObjectInputStream(fis);

            while (true) {
                try {
                    history.add((GlobalMessage) ois.readObject());
                } catch (EOFException ex) {
                    break;
                }

            }

        } catch (FileNotFoundException e) {
            //On est arrivé à la fin du fichier, on passe à la suite
        }


        // Start the TCP server
        startServer(port);
    }

    /**
     * Start a TCP server on the given port
     *
     * @param port the port the server should listen to
     */
    private static void startServer(int port) throws IOException {

        fos = new FileOutputStream(nomFichierSerialization);
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
    public static synchronized void broadcastMessage(GlobalMessage message, Socket socket) throws IOException {

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

            if (client.getSocket() == socket && client.getPseudo() != null) // On envoie l'historique au nouvel arrivant
            {
                if (message.getType().equals("connect")) {
                    for (GlobalMessage gm : history) {
                        try {
                            client.getOutputStream().writeObject(gm);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

        // Add the message in the history
        history.add(message);
        oos.writeObject(message);
        oos.flush();
    }
}
