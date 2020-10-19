package stream.core.requests;

import java.io.Serializable;

public class JoinRoomRequest implements Serializable {
    /**
     * The room name to join
     */
    private final String roomName;

    /**
     * Create a join room request to send to the server
     * @param roomName the room name to join
     */
    public JoinRoomRequest(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Get the room name to join
     * @return the room name to join
     */
    public String getRoomName() {
        return roomName;
    }

}
