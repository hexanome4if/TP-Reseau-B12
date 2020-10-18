package stream.core;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalMessage implements Serializable {
    /**
     * Message type
     */
    private final String type;

    /**
     * Message data corresponding to the message type
     */
    private final Object data;

    /**
     * Message client pseudo (can be empty)
     */
    private final String pseudo;

    /**
     * Message room
     */
    private final String roomName;

    /**
     * Message date
     */
    private String date;

    /**
     * Create a new global message to send over network
     * @param type message type
     * @param data message data
     */
    public GlobalMessage(String type, Object data, String roomName) {
        this.type = type;
        this.data = data;
        this.roomName = roomName;
        this.pseudo = "";
        this.date = "";
    }

    /**
     * Create a new global message with the user pseudo to send over network
     * @param pseudo user pseudo
     * @param type message type
     * @param data message data
     */
    public GlobalMessage(String pseudo, String type, Object data, String roomName) {
        this.pseudo = pseudo;
        this.type = type;
        this.data = data;
        this.roomName = roomName;
    }

    /**
     * Get message type
     * @return message type
     */
    public String getType() {
        return type;
    }

    /**
     * Get message data
     * @return message data
     */
    public Object getData() {
        return data;
    }

    /**
     * Get message pseudo
     * @return message pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Get message date
     * @return message date
     */
    public String getDate() { return date; }

    /**
     * Get message room
     * @return message room
     */
    public String getRoomName() { return roomName; }

    /**
     * Set the message date in a readable format
     */
    public void setDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        this.date = format.format(date);
    }


}
