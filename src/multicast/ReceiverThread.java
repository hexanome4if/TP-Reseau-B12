/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package multicast;

import java.io.*;
import java.net.*;



public class ReceiverThread extends Thread {

  private MulticastSocket multicastSocket;
  private InetAddress groupAddr;
  private int groupPort;
  private boolean run = true;
  private DatagramPacket recv = null;

  ReceiverThread(MulticastSocket s, InetAddress groupAddr, int groupPort) {
    this.multicastSocket = s;
    this.groupPort = groupPort;
    this.groupAddr = groupAddr;
  }

  public void run() {
        try {
          while (run) {
            byte[] buf = new byte[1000];
            recv = new DatagramPacket(buf, buf.length);
            multicastSocket.receive(recv);
            System.out.println(new String(recv.getData()));
          }
        } catch (SocketException se) {

        } catch (Exception e) {
            System.err.println("Error in ReceiverThread:" + e);
            e.printStackTrace();
        }
  }

  public void disconnect() throws IOException {
    run = false;
  }

}
