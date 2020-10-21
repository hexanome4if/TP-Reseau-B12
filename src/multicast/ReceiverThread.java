/***
 * ReceiverThread
 * Receiver thread for a multicast UDP client
 * Date: 17/10/2020
 * Authors: Paul Moine and Fabien Narboux
 */

package multicast;

import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketException;

/**
 *
 */
public class ReceiverThread extends Thread {

    /**
     * multicast socket of the client
     **/
    private final MulticastSocket multicastSocket;
    /**
     * boolean used to run and stop the thread
     **/
    private boolean run = true;

    /**
     * ReceiverThread constructor
     *
     * @param s multicast socket of the client
     **/
    ReceiverThread(MulticastSocket s) {
        this.multicastSocket = s;
    }

    /**
     * Run the thread to receive Datagram packets
     **/
    public void run() {
        try {
            while (run) {
                byte[] buf = new byte[1000];
                DatagramPacket recv = new DatagramPacket(buf, buf.length);
                multicastSocket.receive(recv);
                System.out.println(new String(recv.getData()));
            }
        } catch (SocketException se) {
            // Do nothing
        } catch (Exception e) {
            System.err.println("Error in ReceiverThread:" + e);
            e.printStackTrace();
        }
    }

    /**
     * Stop the thread
     **/
    public void disconnect() {
        run = false;
    }

}
