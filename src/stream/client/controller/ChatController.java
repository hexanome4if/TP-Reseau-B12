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
        GlobalMessage globalMessage = new GlobalMessage("Me", "message", message);
        globalMessage.setDate();
        chatFrame.onReceiveMessage(globalMessage);
    }

    public void receiveMessage(GlobalMessage message) {
        chatFrame.onReceiveMessage(message);
    }
}
