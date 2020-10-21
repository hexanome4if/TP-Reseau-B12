package stream.core.requests;

import java.io.Serializable;

/**
 *
 */
public class CreateRoomRequest implements Serializable {
    /**
     * The room name to create
     */
    private final String roomName;

    /**
     * Create a create room request to send to the server
     * @param roomName the room name to create
     */
    public CreateRoomRequest(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Get the room name to create
     * @return the room name to create
     */
    public String getRoomName() {
        return roomName;
    }
}
