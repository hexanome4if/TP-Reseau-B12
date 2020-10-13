package stream.core;

import java.io.Serializable;

public class GlobalMessage implements Serializable {
    /**
     * Message type can be "message" | "set_pseudo" | "disconnect"
     */
    private final String type;

    /**
     * Message data corresponding to the message type
     */
    private final String data;

    private final String pseudo;

    /**
     * Create a new global message to send over network
     * @param type message type
     * @param data message data
     */
    public GlobalMessage(String type, String data) {
        this.type = type;
        this.data = data;
        this.pseudo = "";
    }

    public GlobalMessage(String pseudo, String type, String data) {
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
    public String getData() {
        return data;
    }

    /**
     * Get message pseudo
     * @return message pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

}
