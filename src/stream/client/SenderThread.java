package stream.client;

import stream.core.GlobalMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class SenderThread extends Thread {

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
    SenderThread(Socket s) {
        this.echoSocket = s;
    }

    /**
     * Start an infinite loop to send messages to the server
     */
    @Override
    public void run() {
        try {
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            socOut = new ObjectOutputStream(echoSocket.getOutputStream());

            // Infinite loop to send messages
            while (run) {
                GlobalMessage message = MainClient.getNextMessageToSend();
                if (message != null) {
                    socOut.writeObject(message);
                }
            }

        } catch (SocketException e) {
          // Do nothing
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
