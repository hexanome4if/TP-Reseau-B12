package stream.core.infos;

import java.io.Serializable;

/**
 *
 */
public class UserJoinedRoomInfo implements Serializable {
    /**
     * User pseudo
     */
    private final String pseudo;

    /**
     * Room name
     */
    private final String roomName;

    /**
     * Create user joined room info to send to clients
     * @param pseudo user pseudo
     * @param roomName room name
     */
    public UserJoinedRoomInfo(String pseudo, String roomName) {
        this.pseudo = pseudo;
        this.roomName = roomName;
    }

    /**
     * Get room name
     * @return room name
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Get user pseudo
     * @return user pseudo
     */
    public String getPseudo() {
        return pseudo;
    }
}
