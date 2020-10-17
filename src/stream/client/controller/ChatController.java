package stream.client.controller;

import stream.client.MainClient;
import stream.client.ReceiverThread;
import stream.client.view.ChatView;
import stream.core.FileMessage;
import stream.core.GlobalMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ChatController {
    /**
     * Reference to the chat view
     */
    private final ChatView chatView;

    /**
     * Create a new ChatController to handle the connection between the view and the server
     * @param chatView chat view
     */
    public ChatController(ChatView chatView) {
        this.chatView = chatView;
        ReceiverThread.getInstance().registerChatController(this);
    }

    /**
     * Disconnect the user
     */
    public void disconnect() {
        MainClient.send(new GlobalMessage("disconnect", null));
        MainClient.disconnect();
        chatView.close();
        System.exit(0);
    }

    /**
     * Send a message from the user
     * @param message message to send
     */
    public void sendMessage(String message) {
        MainClient.send(new GlobalMessage("message-txt", message));
        GlobalMessage globalMessage = new GlobalMessage("Me", "message-txt", message);
        globalMessage.setDate();
        chatView.onReceiveMessage(globalMessage);
    }

    /**
     * Send a message containing a file
     * @param file the file to send
     * @throws IOException
     */
    public void sendFile(File file) throws IOException {
        MainClient.send(new GlobalMessage("message-file", new FileMessage(file.getName(), Files.readAllBytes(file.toPath()))));
        GlobalMessage globalMessage = new GlobalMessage("Me", "message-file", new FileMessage(file.getName(), Files.readAllBytes(file.toPath())));
        globalMessage.setDate();
        chatView.onReceiveMessage(globalMessage);
    }

    /**
     * Receive a message from the server for the user
     * @param message the message received
     */
    public void receiveMessage(GlobalMessage message) {
        chatView.onReceiveMessage(message);
    }
}
