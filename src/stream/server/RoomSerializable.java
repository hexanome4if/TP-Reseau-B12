package stream.server;

import java.io.Serializable;

/**
 *
 */
public class RoomSerializable implements Serializable {
    /**
     * The room name
     */
    private final String roomName;

    /**
     * Create a new object representing a room to serialize
     * @param roomName the room name
     */
    public RoomSerializable(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Get the room name
     * @return the room name
     */
    public String getRoomName() {
        return roomName;
    }

}
