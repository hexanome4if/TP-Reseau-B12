package stream.core.infos;

import java.io.Serializable;

public class UserInfo implements Serializable {
    /**
     * User id
     */
    private final String id;
    /**
     * User pseudo
     */
    private final String pseudo;

    /**
     * Create a new user info
     * @param id user id
     * @param pseudo user pseudo
     */
    public UserInfo(String id, String pseudo) {
        this.id = id;
        this.pseudo = pseudo;
    }

    /**
     * Get user id
     * @return user id
     */
    public String getId() {
        return id;
    }

    /**
     * Get user pseudo
     * @return user pseudo
     */
    public String getPseudo() {
        return pseudo;
    }
}
