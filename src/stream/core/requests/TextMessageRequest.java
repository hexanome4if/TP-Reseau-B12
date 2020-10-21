package stream.core.requests;

import java.io.Serializable;

public class TextMessageRequest implements Serializable {
    /**
     * The message content
     */
    private final String message;

    /**
     * Create a new text message request to send to the server
     * @param message the message content
     */
    public TextMessageRequest(String message) {
        this.message = message;
    }

    /**
     * Get message content
     * @return message content
     */
    public String getMessage() {
        return message;
    }
}
