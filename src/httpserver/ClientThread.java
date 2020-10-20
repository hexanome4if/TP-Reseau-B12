package httpserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread {

    private final Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

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

            String str = ".";
            byte[] b;
            List<String> headers = new ArrayList<>();

            // Read headers
            while (str != null && !str.equals("")) {
                str="";
                do {
                  b = inputStream.readNBytes(1);
                  str += new String(b);
                } while (str.length() < 2 || str.charAt(str.length()-2) != '\r' || str.charAt(str.length()-1) != '\n' );
                str = str.substring(0, str.length()-2);
                headers.add(str);
                System.out.println(str);
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
                byteBody = new byte[intContentLength];
                byteBody = inputStream.readNBytes(intContentLength);
            }

            System.out.println("Body : ");
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
