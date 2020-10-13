package stream.server;

import stream.core.GlobalMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

/**
 * @author polo
 */
public class MainServer {

    /**
     * Start a TCP server on the port specified as the first CLI argument
     * @param args CLI arguments
     */
    public static void main(String[] args) {
        // Check CLI arguments
        if (args.length != 1) {
            System.out.println("Usage: java EchoServer <EchoServer port>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);

        // Start the TCP server
        startServer(port);
    }

    /**
     * Start a TCP server on the given port
     * @param port the port the server should listen to
     */
    private static void startServer(int port) {
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
            System.err.println("Error in EchoServer:" + e);
        }
    }

    /**
     * Broadcast a message to every connected client except the one identified by socket
     * @param message message to send
     * @param socket client socket to ignore during broadcast
     */
    public static synchronized void broadcastMessage(GlobalMessage message, Socket socket) {

        // Get every connected clients
        Collection<ClientData> clients = ClientContainer.getClients();

        for(ClientData client : clients) {
            // Check if the client is not the one which should be ignored
            if (client.getSocket() != socket && client.getPseudo() != null) {
                try {
                    client.getOutputStream().writeObject(message); // Send the message
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
