/***
 * SenderThread
 * Sender thread for a multicast UDP client
 * Date: 17/10/2020
 * Authors: Paul Moine and Fabien Narboux
 */

package multicast;

import java.io.*;
import java.net.*;



public class SenderThread {

  /**
  * multicast socket of the client
  **/
  private MulticastSocket multicastSocket;
  /**
  * adress group of the client
  **/
  private InetAddress groupAddr;
  /**
  * port group of the client
  **/
  private int groupPort;
  /**
  * boolean used to run and stop the thread
  **/
  private boolean run = true;
  /**
  * datagram packet sent
  **/
  private DatagramPacket packet = null;

  /**
  * SenderThread constructor
  * @param s multicast socket of the client
  * @param groupAddr group adresse of the client
  * @param groupPort group port of the client
  **/

  SenderThread(MulticastSocket s, InetAddress groupAddr, int groupPort) {
    this.multicastSocket = s;
    this.groupPort = groupPort;
    this.groupAddr = groupAddr;
  }

  /**
  *  Run the thread to send Datagram packets
  **/
  public void run() {
        BufferedReader stdIn = null;
    	  try {
        		stdIn = new BufferedReader(new InputStreamReader(System.in));
            String line;
            System.out.println("Pseudo : ");
            line=stdIn.readLine();
            System.out.println("You are connected with the pseudo : " + line);
            System.out.println("");
            String pseudo = line;
            String msg = line + " join the group !";
            packet = new DatagramPacket(msg.getBytes(), msg.length(), groupAddr, groupPort);
            multicastSocket.send(packet);

            while (run) {
            	line=stdIn.readLine();
              if(line.equals("disconnect")) {
                run = false;
                msg = pseudo + " left the group !";
                packet = new DatagramPacket(msg.getBytes(), msg.length(), groupAddr, groupPort);
                multicastSocket.send(packet);
              } else {
                line = pseudo + " : " + line;
                packet = new DatagramPacket(line.getBytes(), line.length(), groupAddr, groupPort);
                multicastSocket.send(packet);
              }
            }
      	} catch (Exception e) {
          	System.err.println("Error in SenderThread:" + e);
            e.printStackTrace();
        }
  }
}