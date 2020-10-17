package stream.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author polo
 */
public class ClientData {
    /**
     * Client socket
     */
    private final Socket socket;

    /**
     * Object output stream to send a message to the client
     */
    private ObjectOutputStream outputStream;

    /**
     * Client pseudo
     */
    private String pseudo;

    /**
     * Create a new client data for a given client represented by it's socket
     * @param socket the client socket
     */
    public ClientData(Socket socket) {
        this.socket = socket;
        this.pseudo = null;
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the client socket
     * @return client socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Get the output stream to send a message to the client
     * @return object output stream
     */
    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Get client pseudo
     * @return client pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Set client pseudo
     * @param pseudo new client pseudo
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }



}
