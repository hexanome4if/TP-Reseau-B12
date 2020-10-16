package stream.client;

import java.io.*;
import java.net.*;

import stream.client.controller.ChatController;
import stream.core.*;

public class ReceiverThread extends Thread {

    /**
     * Socket connection to server
     */
    private final Socket echoSocket;
    /**
     * Infinite loop manager
     */
    private boolean run = true;
    /**
     * Input stream from server
     */
    private ObjectInputStream socIn = null;

    private ChatController chatController;

    private static ReceiverThread instance;

    public static ReceiverThread getInstance() {
        return instance;
    }

    /**
     * Create a thread to receive messages from the server
     * @param s server socket connection
     */
    ReceiverThread(Socket s) {
        this.echoSocket = s;
        instance = this;
    }

    public void registerChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    @Override
    public void run() {
        try {
            socIn = new ObjectInputStream(echoSocket.getInputStream());
            // Infinite loop to receive messages
            while (run) {
                // Get the GlobalMessage
                GlobalMessage message = (GlobalMessage) socIn.readObject();
                // Handle the message
                if (chatController != null) {
                    chatController.receiveMessage(message);
                }
            }
        } catch (SocketException se) {
            // Ignore exception
        } catch (Exception e) {
            System.err.println("Error in ReceiverThread:" + e);
            e.printStackTrace();
        }
    }

    /**
     * Method used when the client disconnect from the server to clear stream and stop infinite loop
     * @throws IOException
     */
    public void disconnect() throws IOException {
        run = false;
        socIn.close();
    }
}
