package httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            // read the data sent. We basically ignore it,
            // stop reading once a blank line is hit. This
            // blank line signals the end of the client HTTP
            // headers.

            String str = ".";
            List<String> headers = new ArrayList<>();

            // Read headers
            while (str != null && !str.equals("")) {
                str = in.readLine();
                headers.add(str);
                System.out.println(str);
            }
            if (headers.size() == 0 || headers.get(0) == null) {
                socket.close();
                return;
            }

            Request request = new Request(headers);
            String contentLength = request.getHeader("Content-Length");
            char[] body = null;
            if (contentLength != null) {
                int intContentLength = Integer.parseInt(contentLength);
                body = new char[intContentLength];
                in.read(body, 0, intContentLength);
            }

            System.out.println("Body : ");
            if (body != null) {
                System.out.println(body);
                request.setBody(new String(body));
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
