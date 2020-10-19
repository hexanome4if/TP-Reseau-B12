package stream.client.view;

import stream.client.controller.ChatController;
import stream.core.GlobalMessage;

import javax.swing.*;
import java.awt.*;

public class ChatView {

    /**
     * Reference to the chat controller to handle user actions
     */
    private final ChatController chatController;

    /**
     * Reference to the chat panel which renders the chat
     */
    private ChatPanel chatPanel;

    /**
     * Reference to the room management panel which renders the room management
     */
    private RoomManagementPanel roomManagementPanel;

    /**
     * Actual window
     */
    private JFrame frame;


    /**
     * Create a new chat view but does not show it
     */
    public ChatView() {
        chatController = new ChatController(this);
    }

    /**
     * Render the view
     */
    public void show() {
        frame = new JFrame("INSAChat - Chat");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setBackground(new Color(255, 255, 255));
        frame.setVisible(true);

        chatPanel = new ChatPanel(this, chatController, frame);
        roomManagementPanel = new RoomManagementPanel(this, chatController, frame);

    }

    /**
     * Close the window
     */
    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }

    /**
     * Event to call when a new message is received to show it on the view
     * @param message the message received
     */
    public void onReceiveMessage(GlobalMessage message) {
        System.out.println("Received message: " + message.getType());
        // Important to be thread safe
        SwingUtilities.invokeLater(() -> {
            switch (message.getType()) {
                case "room-new": {
                    roomManagementPanel.onGetRoom(message);
                    break;
                }
                default: {
                    chatPanel.onReceiveMessage(message);
                    break;
                }
            }
        });
    }

    /**
     * Switch current view to the chat panel
     */
    public void renderChatPanel() {
        frame.setContentPane(chatPanel.render());
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Switch current view to the room management panel
     */
    public void renderRoomManagementPanel() {
        frame.setContentPane(roomManagementPanel.render());
        frame.revalidate();
        frame.repaint();
    }



}
