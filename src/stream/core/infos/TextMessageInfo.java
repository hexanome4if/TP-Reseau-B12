package stream.core.infos;

import java.io.Serializable;

public class TextMessageInfo implements Serializable {

    /**
     * Message content
     */
    private final String content;

    /**
     * Create a new text message info to send to the clients
     * @param content the text message content
     */
    public TextMessageInfo(String content) {
        this.content = content;
    }

    /**
     * Get message text content
     * @return text content
     */
    public String getContent() {
        return content;
    }
}
