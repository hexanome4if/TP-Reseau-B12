package stream.core.requests;

import java.io.Serializable;

public class MessageRequest implements Serializable {
    /**
     * The room name in which we want to send the message
     */
    private final String roomName;

    /**
     * Message type (can be text or file)
     */
    private final String type;

    /**
     * The message data
     */
    private final Object data;

    /**
     * Create a message request to send to the server
     * @param roomName the room name in which we want to send the message
     * @param data the message data
     */
    public MessageRequest(String roomName, String type, Object data) {
        this.roomName = roomName;
        this.type = type;
        this.data = data;
    }

    public String getRoomName() {
        return roomName;
    }

    public Object getData() {
        return data;
    }

    public String getType() {
        return type;
    }
}
