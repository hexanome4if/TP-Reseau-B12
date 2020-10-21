package stream.core.requests;

import java.io.Serializable;

public class ConnectRequest implements Serializable {

    /**
     * The user pseudo
     */
    private final String pseudo;

    /**
     * Create a new connect request to send to the server
     * @param pseudo the user pseudo
     */
    public ConnectRequest(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Get user pseudo
     * @return user pseudo
     */
    public String getPseudo() {
        return pseudo;
    }
}
