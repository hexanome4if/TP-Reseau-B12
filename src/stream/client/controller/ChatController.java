package stream.client.controller;

import stream.client.MainClient;
import stream.client.ReceiverThread;
import stream.client.SenderThread;
import stream.client.view.ChatFrame;
import stream.core.GlobalMessage;

public class ChatController {
    private final ChatFrame chatFrame;

    public ChatController(ChatFrame chatFrame) {
        this.chatFrame = chatFrame;
        ReceiverThread.getInstance().registerChatController(this);
    }

    public void disconnect() {
        MainClient.send(new GlobalMessage("disconnect", null));
        MainClient.disconnect();
        chatFrame.close();
        System.exit(0);
    }

    public void sendMessage(String message) {
        MainClient.send(new GlobalMessage("message", message));
        chatFrame.onReceiveMessage(new GlobalMessage("Me", "message", message));
    }

    public void receiveMessage(GlobalMessage message) {
        System.out.println("Message received in controller");
        chatFrame.onReceiveMessage(message);
    }
}
