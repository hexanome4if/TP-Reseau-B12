package stream.client;

import stream.client.view.PseudoView;
import stream.core.GlobalMessage;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 *
 */
public class MainClient {

    /**
     * Representation of the thread which receives messages from the server
     */
    private static ReceiverThread receiverThread;
    /**
     * Representation of the thread which send messages to the server
     */
    private static SenderThread senderThread;
    /**
     * Socket connection to the server
     */
    private static Socket echoSocket;

    /**
     * Send queue to handle messages to send to the server
     */
    private static final Queue<GlobalMessage> sendQueue = new LinkedList<>();
    /**
     * Lock to ensure there's no conflict while accessing the send queue
     */
    private static final Semaphore sendQueueLock = new Semaphore(1);


    /**
     * Add a message to send in the send queue
     * @param message the message to send
     */
    public static void send(GlobalMessage message) {
        try {
            sendQueueLock.acquire();
            sendQueue.add(message);
            sendQueueLock.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the next message to send to the server
     * @return the next message to send or null if there's none
     */
    public static GlobalMessage getNextMessageToSend() {
        try {
            sendQueueLock.acquire();
            if (sendQueue.size() > 0) {
                GlobalMessage message = sendQueue.remove();
                sendQueueLock.release();
                return message;
            }
            sendQueueLock.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Start a chat client which will connect to a TCP server
     * @param args CLI arguments
     */
    public static void main(String[] args) {
        // Check CLI arguments
        if (args.length != 2) {
            System.out.println("Usage: java main <EchoServer host> <EchoServer port>");
            System.exit(1);
        }

        // Get CLI arguments
        String host = args[0];
        Integer port = Integer.parseInt(args[1]);

        // Create the connection to the server
        echoSocket = connectToServer(host, port);

        // Start receiver thread
        receiverThread = new ReceiverThread(echoSocket);
        receiverThread.start();

        // Start sender loop
        senderThread = new SenderThread(echoSocket);
        senderThread.start();

        // Show pseudo frame
        new PseudoView().show();

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
     */
    public static void disconnect() {
        try {
            senderThread.disconnect();
            receiverThread.disconnect();
            echoSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
