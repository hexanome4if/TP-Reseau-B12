package stream.core;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalMessage implements Serializable {
    /**
     * Message type can be "message" | "set_pseudo" | "disconnect"
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
     * Message date
     */
    private String date;

    /**
     * Create a new global message to send over network
     * @param type message type
     * @param data message data
     */
    public GlobalMessage(String type, Object data) {
        this.type = type;
        this.data = data;
        this.pseudo = "";
        this.date = "";
    }

    /**
     * Create a new global message with the user pseudo to send over network
     * @param pseudo user pseudo
     * @param type message type
     * @param data message data
     */
    public GlobalMessage(String pseudo, String type, Object data) {
        this.pseudo = pseudo;
        this.type = type;
        this.data = data;
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
     * Set the message date in a readable format
     */
    public void setDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        this.date = format.format(date);
    }
}
