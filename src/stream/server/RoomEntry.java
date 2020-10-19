package stream.server;

import stream.core.infos.UserInfo;
import stream.core.infos.UserJoinedRoomInfo;

import java.util.ArrayList;
import java.util.List;

public class RoomEntry {

    /**
     * Room name
     */
    private final String name;

    /**
     * List of users in this room
     */
    private final List<String> users = new ArrayList<>();

    /**
     * Create a new room entry
     * @param name room name
     */
    public RoomEntry(String name) {
        this.name = name;
    }

    /**
     * Get room name
     * @return room name
     */
    public String getName() {
        return name;
    }

    /**
     * Add a user in this room
     * @param userId user id
     */
    public void addUser(String userId) {
        users.add(userId);
    }

    /**
     * Remove a user from this room
     * @param userId user id
     */
    public void removeUser(String userId) {
        users.remove(userId);
    }

    /**
     * Get users in this room
     * @return users in this room
     */
    public List<String> getUsers() {
        return users;
    }

    public UserInfo[] getUsersFormatted() {
        UserInfo[] users = new UserInfo[this.users.size()];
        int i = 0;
        for(String userId: this.users) {
            ClientData clientData = ClientContainer.getClient(userId);
            if (clientData != null) {
                users[i++] = new UserInfo(userId, clientData.getPseudo());
            }
        }
        return users;
    }
}
