package stream.client;

import stream.core.infos.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomEntry {
    /**
     * Room name
     */
    private final String roomName;

    /**
     * Users in the room
     */
    private final List<UserInfo> users;

    /**
     * Create a new room
     * @param roomName room name
     * @param users users in the room
     */
    public RoomEntry(String roomName, UserInfo[] users) {
        this.roomName = roomName;
        this.users = Arrays.asList(users);
    }

    /**
     * Get room name
     * @return room name
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Get users in the room
     * @return users in the room
     */
    public List<UserInfo> getUsers() {
        return users;
    }

    /**
     * Add a user in the room
     * @param user the user to add
     */
    public void addUser(UserInfo user) {
        users.add(user);
    }

    /**
     * Remove a user from the room
     * @param user the user to remove
     */
    public void removeUser(UserInfo user) {
        users.removeIf(userInfo -> userInfo.getId().equals(user.getId()));
    }
}
