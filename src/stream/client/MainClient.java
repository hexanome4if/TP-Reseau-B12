/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package stream.client;

import java.io.*;
import java.net.*;



public class MainClient {


  /**
  *  main method
  *  accepts a connection, receives a message from client then sends an echo to the client
  **/
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        boolean run = true;

        if (args.length != 2) {
          System.out.println("Usage: java main <EchoServer host> <EchoServer port>");
          System.exit(1);
        }

        String host = args[0];
        Integer port = new Integer(args[1]).intValue();

        echoSocket = connectToServer(echoSocket, host, port);

        String line;
        ReceiverThread rt = null;
        SenderThread st = null;

        rt = new ReceiverThread(echoSocket);
        rt.start();
        st = new SenderThread(echoSocket);
        st.run();


        rt.disconnect();
        st.disconnect();
        disconnect(echoSocket);

    }

    public static Socket connectToServer(Socket echoSocket, String host, Integer port)  {
        try {
            // creation socket ==> connexion
            echoSocket = new Socket(host,port);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for " + "the connection to:"+ host);
            System.exit(1);
        }
        return echoSocket;
    }

    public static void disconnect(Socket echoSocket) throws IOException {
      echoSocket.close();
    }
}
