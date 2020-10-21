package stream.client;

import stream.client.controller.ChatController;
import stream.core.GlobalMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

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

    /**
     * Chat controller to send received messages to
     */
    private ChatController chatController;

    /**
     * Instance for the singleton
     */
    private static ReceiverThread instance;

    /**
     * Get instance (singleton)
     * @return the current receiver thread instance
     */
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

    /**
     * Register the chat controller to send an event when a new message is received
     * @param chatController the chat controller
     */
    public void registerChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    /**
     * Receive messages from the server and send them to the ChatController
     */
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
     * @throws IOException if an error occurred while closing the socket input stream
     */
    public void disconnect() throws IOException {
        run = false;
        socIn.close();
    }
}
