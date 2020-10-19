/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package multicast;

import java.io.*;
import java.net.*;



public class MainMulticast {


  /**
  *  main method
  *  accepts a connection, receives a message from client then sends an echo to the client
  **/
    public static void main(String[] args) throws IOException {

        MulticastSocket multicastSocket = null;
        InetAddress groupAddr = InetAddress.getByName("228.5.6.7");
        int groupPort = 9001;

        multicastSocket = connectToServer(multicastSocket, groupAddr, groupPort);

        ReceiverThread rt = null;
        SenderThread st = null;

        rt = new ReceiverThread(multicastSocket, groupAddr, groupPort);
        rt.start();
        st = new SenderThread(multicastSocket, groupAddr, groupPort);
        st.run();


        rt.disconnect();
        st.disconnect();
        disconnect(multicastSocket, groupAddr);

    }

    public static MulticastSocket connectToServer(MulticastSocket multicastSocket, InetAddress groupAddr, int groupPort)  {
        try {
            // creation socket ==> connexion
            multicastSocket = new MulticastSocket(groupPort);
            multicastSocket.joinGroup(groupAddr);
        } catch (Exception e) {
            System.err.println("Error in connection to multicast : " + e);
            System.exit(1);
        }
        return multicastSocket;
    }

    public static void disconnect(MulticastSocket multicastSocket, InetAddress groupAddr) throws IOException {
      multicastSocket.leaveGroup(groupAddr);
    }
}
