package stream.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
     * Client unique id
     */
    private final String id;

    /**
     * Object output stream to send a message to the client
     */
    private ObjectOutputStream outputStream;

    /**
     * Client pseudo
     */
    private String pseudo;

    /**
     * Client rooms
     */
    private final List<String> rooms;

    /**
     * Create a new client data for a given client represented by it's socket
     * @param socket the client socket
     */
    public ClientData(Socket socket) {
        this.socket = socket;
        this.pseudo = null;
        this.rooms = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
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

    /**
     * Add a room to the client rooms list
     * @param roomName the room name to add
     */
    public void joinRoom(String roomName) {
        rooms.add(roomName);
    }

    /**
     * Remove a room from the client rooms list
     * @param roomName the room name to remove
     */
    public void leaveRoom(String roomName) {
        rooms.remove(roomName);
    }

    /**
     * Get client joined rooms
     * @return client joined rooms
     */
    public List<String> getJoinedRooms() {
        return rooms;
    }

    /**
     * Get user unique id
     * @return user id
     */
    public String getId() {
        return id;
    }
}
