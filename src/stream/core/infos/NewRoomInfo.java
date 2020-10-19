package stream.core.infos;

import java.io.Serializable;

public class NewRoomInfo implements Serializable {
    /**
     * Room name
     */
    private final String roomName;

    /**
     * Users in the room
     */
    private final UserInfo[] users;

    /**
     * Create a new room info to send to the clients
     * @param roomName room name
     * @param users users in the room
     */
    public NewRoomInfo(String roomName, UserInfo[] users) {
        this.roomName = roomName;
        this.users = users;
    }

    /**
     * Get the room name
     * @return room name
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Get users in the room
     * @return users in the room
     */
    public UserInfo[] getUsers() {
        return users;
    }
}
