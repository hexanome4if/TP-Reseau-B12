/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package multicast;

import java.io.*;
import java.net.*;



public class SenderThread {

  private MulticastSocket multicastSocket;
  private InetAddress groupAddr;
  private int groupPort;
  private boolean run = true;
  private DatagramPacket packet = null;

  SenderThread(MulticastSocket s, InetAddress groupAddr, int groupPort) {
    this.multicastSocket = s;
    this.groupPort = groupPort;
    this.groupAddr = groupAddr;
  }

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
              msg = line + " left the group !";
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

  public void disconnect() throws IOException {

  }

}
