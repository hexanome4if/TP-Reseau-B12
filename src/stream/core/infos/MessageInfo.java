package stream.core.infos;

import java.io.Serializable;

/**
 *
 */
public class MessageInfo implements Serializable {
    /**
     * Message type (can be text or file)
     */
    private final String type;

    /**
     * Message data
     */
    private final Object data;

    /**
     * User pseudo
     */
    private final String userPseudo;

    /**
     * Room name
     */
    private final String roomName;

    /**
     * Create a new message info to send to the clients
     * @param type the message type
     * @param userPseudo the sender user pseudo
     * @param data the message content
     * @param roomName the message room name
     */
    public MessageInfo(String type, Object data, String userPseudo, String roomName) {
        this.type = type;
        this.userPseudo = userPseudo;
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
     * Get message content
     * @return message content
     */
    public Object getData() {
        return data;
    }

    /**
     * Get the message sender user pseudo
     * @return user pseudo
     */
    public String getUserPseudo() {
        return userPseudo;
    }

    /**
     * Get the message room name
     * @return room name
     */
    public String getRoomName() {
        return roomName;
    }
}
