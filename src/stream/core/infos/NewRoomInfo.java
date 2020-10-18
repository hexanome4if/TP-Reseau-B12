package stream.core.infos;

import java.io.Serializable;

public class NewRoomInfo implements Serializable {
    /**
     * Room name
     */
    private final String roomName;

    /**
     * Create a new room info to send to the clients
     * @param roomName room name
     */
    public NewRoomInfo(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Get the room name
     * @return room name
     */
    public String getRoomName() {
        return roomName;
    }
}
