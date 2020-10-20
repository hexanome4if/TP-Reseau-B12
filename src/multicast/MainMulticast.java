/***
 * MainMulticast
 * Multicast UDP client
 * Date: 17/10/2020
 * Authors: Paul Moine and Fabien Narboux
 */

package multicast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;



public class MainMulticast {

  /**
  * multicast socket of the client
  **/
  private static MulticastSocket multicastSocket = null;
  /**
  * adress group of the client
  **/
  private static InetAddress groupAddr = null;
  /**
  * port group of the client
  **/
  private static int groupPort = 9001;
  /**
  * IP adress for the multicast
  **/
  private static String multicastIPAdress = "228.5.6.7";

  /**
  *  main method
  *  Connection to the multicast group
  * @throws IOException
  **/
    public static void main(String[] args) throws IOException {

        groupAddr = InetAddress.getByName(multicastIPAdress);

        connectToGroup();

        ReceiverThread rt = null;
        SenderThread st = null;

        rt = new ReceiverThread(multicastSocket);
        rt.start();
        st = new SenderThread(multicastSocket, groupAddr, groupPort);
        st.run();


        rt.disconnect();
        disconnect();

    }

    /**
    * Connect the client to the group
    **/
    public static void connectToGroup()  {
        try {
            // creation socket ==> connexion
            multicastSocket = new MulticastSocket(groupPort);
            multicastSocket.joinGroup(groupAddr);
        } catch (Exception e) {
            System.err.println("Error in connection to multicast : " + e);
            System.exit(1);
        }
    }

    /**
    * Disconnect the client to the group
    **/
    public static void disconnect() throws IOException {
      multicastSocket.leaveGroup(groupAddr);
    }
}
