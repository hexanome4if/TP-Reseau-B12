/**
 * SenderThread
 * Sender thread for a multicast UDP client
 * Date: 17/10/2020
 * Authors: Paul Moine and Fabien Narboux
 */

package httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ClientThread extends Thread {

    /**
    * socket of the server
    **/
    private final Socket socket;

    /**
    * Constructor the client thread
    * @param socket socket of the server
    **/
    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    /**
    * Run the thread, it will read data in input and parse it in a Request object then send a response
    **/
    @Override
    public void run() {
        try {
            // remote is now the connected socket
            System.out.println("Connection, sending data.");
            InputStream inputStream = socket.getInputStream();


            // read the data sent. We basically ignore it,
            // stop reading once a blank line is hit. This
            // blank line signals the end of the client HTTP
            // headers.

            StringBuilder str = new StringBuilder(".");
            byte[] b;
            List<String> headers = new ArrayList<>();

            // Read headers
            while (!str.toString().equals("")) {
                str = new StringBuilder();
                do {
                  b = inputStream.readNBytes(1);
                  str.append(new String(b));
                } while (str.length() < 2 || str.charAt(str.length()-2) != '\r' || str.charAt(str.length()-1) != '\n' );
                str = new StringBuilder(str.substring(0, str.length() - 2));
                headers.add(str.toString());
            }
            if (headers.size() == 0 || headers.get(0) == null) {
                socket.close();
                return;
            }

            Request request = new Request(headers);
            String contentLength = request.getHeader("Content-Length");
            byte[] byteBody = null;
            if (contentLength != null) {
                int intContentLength = Integer.parseInt(contentLength);
                byteBody = inputStream.readNBytes(intContentLength);
            }

            if (byteBody != null) {
                request.setBody(new String(byteBody));
                request.setByteBody(byteBody);
            }

            // Execute request and send response
            Response response = request.executeRequest();
            response.execute(socket.getOutputStream());

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
