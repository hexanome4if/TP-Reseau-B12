package stream.core.requests;

import java.io.Serializable;

/**
 *
 */
public class LeaveRoomRequest implements Serializable {
    /**
     * The room name to leave
     */
    private final String roomName;

    /**
     * Create a leave room request to send to the server
     * @param roomName the room name to leave
     */
    public LeaveRoomRequest(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Get the room name to leave
     * @return the room name to leave
     */
    public String getRoomName() {
        return roomName;
    }

}
