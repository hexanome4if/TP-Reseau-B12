package stream.client.controller;

import stream.client.MainClient;
import stream.client.ReceiverThread;
import stream.client.view.ChatView;
import stream.core.GlobalMessage;
import stream.core.infos.FileMessageInfo;
import stream.core.infos.MessageInfo;
import stream.core.infos.TextMessageInfo;
import stream.core.requests.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ChatController {
    /**
     * List of user rooms
     */
    private final java.util.List<String> joinedRooms = new ArrayList<>();

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
        MainClient.send(new GlobalMessage("disconnect", new DisconnectRequest()));
        MainClient.disconnect();
        chatView.close();
        System.exit(0);
    }

    /**
     * Send a message from the user
     * @param message message to send
     */
    public void sendMessage(String message, String roomName) {
        MainClient.send(new GlobalMessage("message", new MessageRequest(roomName, "text", new TextMessageRequest(message))));
        GlobalMessage globalMessage = new GlobalMessage("message", new MessageInfo("text", new TextMessageInfo(message), "Me", roomName));
        globalMessage.setDate();
        chatView.onReceiveMessage(globalMessage);
    }

    /**
     * Send a message containing a file
     * @param file the file to send
     * @throws IOException
     */
    public void sendFile(File file, String roomName) throws IOException {
        MainClient.send(new GlobalMessage("message", new MessageRequest(roomName, "file", new FileMessageRequest(file.getName(), Files.readAllBytes(file.toPath())))));
        GlobalMessage globalMessage = new GlobalMessage("message", new MessageInfo("file", new FileMessageInfo(file.getName(), Files.readAllBytes(file.toPath())),"Me", roomName));
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

    /**
     * Create a new room on the server
     * @param name room name
     */
    public void createRoom(String name) {
        MainClient.send(new GlobalMessage("create-room", new CreateRoomRequest(name)));
    }

    /**
     * Get user joined rooms
     * @return user joined rooms
     */
    public List<String> getJoinedRooms() {
        return joinedRooms;
    }

    /**
     * Join a room
     * @param roomName the room name to join
     */
    public void joinRoom(String roomName) {
        MainClient.send(new GlobalMessage("join-room", new JoinRoomRequest(roomName)));
        joinedRooms.add(roomName);
    }

    /**
     * Leave a room
     * @param roomName the room name to leave
     */
    public void leaveRoom(String roomName) {
        MainClient.send(new GlobalMessage("leave-room", new LeaveRoomRequest(roomName)));
        joinedRooms.remove(roomName);
    }
}
