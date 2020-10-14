package stream.client;

import java.io.*;
import java.net.*;



public class MainClient {

    /**
     * Start a chat client which will connect to a TCP server
     * @param args CLI arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // Check CLI arguments
        if (args.length != 2) {
            System.out.println("Usage: java main <EchoServer host> <EchoServer port>");
            System.exit(1);
        }

        // Get CLI arguments
        String host = args[0];
        Integer port = Integer.parseInt(args[1]);

        // Create the connection to the server
        Socket echoSocket = connectToServer(host, port);

        // Start receiver thread
        ReceiverThread rt = new ReceiverThread(echoSocket);
        rt.start();

        // Start sender loop
        SenderManager st = new SenderManager(echoSocket);
        st.run();

        // Disconnect when client asks
        rt.disconnect();
        st.disconnect();
        disconnect(echoSocket);
    }

    /**
     * Create a socket connection to a TCP server
     * @param host server host
     * @param port server port
     * @return socket connection to the server
     */
    public static Socket connectToServer(String host, Integer port) {
        Socket echoSocket = null;
        try {
            echoSocket = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for " + "the connection to:" + host);
            System.exit(1);
        }
        return echoSocket;
    }

    /**
     * Disconnect client from server
     * @param echoSocket Socket connection to the server
     * @throws IOException
     */
    public static void disconnect(Socket echoSocket) throws IOException {
        echoSocket.close();
    }
}
