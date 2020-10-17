package stream.client.controller;

import stream.client.MainClient;
import stream.client.ReceiverThread;
import stream.client.SenderThread;
import stream.client.view.ChatFrame;
import stream.client.view.ChatView;
import stream.core.GlobalMessage;

public class ChatController {
    private final ChatView chatView;

    public ChatController(ChatView chatView) {
        this.chatView = chatView;
        ReceiverThread.getInstance().registerChatController(this);
    }

    public void disconnect() {
        MainClient.send(new GlobalMessage("disconnect", null));
        MainClient.disconnect();
        chatView.close();
        System.exit(0);
    }

    public void sendMessage(String message) {
        MainClient.send(new GlobalMessage("message", message));
        GlobalMessage globalMessage = new GlobalMessage("Me", "message", message);
        globalMessage.setDate();
        chatView.onReceiveMessage(globalMessage);
    }

    public void receiveMessage(GlobalMessage message) {
        chatView.onReceiveMessage(message);
    }
}
