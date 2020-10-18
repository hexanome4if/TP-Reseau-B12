package stream.client.view;

import stream.client.controller.ChatController;
import stream.client.view.utils.ColorUtil;
import stream.client.view.utils.FontUtil;
import stream.core.GlobalMessage;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RoomManagementPanel {

    /**
     * The chat view which control the current view
     */
    private final ChatView chatView;
    /**
     * The chat control to handle user actions
     */
    private final ChatController chatController;
    /**
     * The current window
     */
    private final JFrame frame;
    /**
     * Map of the room components with their name as key
     */
    private final Map<String, JPanel> roomEntries = new HashMap<>();

    /**
     * The global panel representing the view
     */
    private JPanel view;
    /**
     * The room container containing all the available rooms
     */
    private JPanel roomContainer;
    /**
     * Input field to set the name of the room to create
     */
    private JTextField roomNameIF;
    /**
     * Button to create a new room
     */
    private JButton createRoomBTN;
    /**
     * Button to go back to the chat panel
     */
    private JButton backBTN;
    /**
     * Constraints to show the room lists
     */
    private GridBagConstraints roomListConstraints;

    /**
     * Create a new room management panel
     * @param chatView the chat view which control the current view
     * @param chatController the chat controller which handle user actions
     * @param frame the current window
     */
    public RoomManagementPanel(ChatView chatView, ChatController chatController, JFrame frame) {
        this.chatView = chatView;
        this.chatController = chatController;
        this.frame = frame;

        createComponents();
    }

    /**
     * Render the panel and update components if needed
     * @return the panel to show
     */
    public JPanel render() {
        backBTN.setEnabled(chatController.getJoinedRooms().size() > 0);
        return view;
    }

    /**
     * Show a new created room
     * @param message the message representing the creation of the new room
     */
    public void onGetRoom(GlobalMessage message) {
        JPanel newRoomPanel = new JPanel();

        renderRoom((String)message.getData(), newRoomPanel);
        roomContainer.add(newRoomPanel, roomListConstraints);
        ++roomListConstraints.gridy;
        roomEntries.put((String) message.getData(), newRoomPanel);

        roomContainer.revalidate();
        roomContainer.repaint();
    }

    /**
     * Initialize components for the view
     */
    private void createComponents() {
        view = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;

        view.add(new JPanel(), constraints);

        constraints.gridy = 1;
        view.add(new JPanel(), constraints);

        constraints.gridx = 1;
        constraints.weighty = 4;
        constraints.weightx = 6;
        view.add(renderMain(), constraints);

        constraints.gridx = 2;
        constraints.weightx = 1;
        constraints.weighty = 1;
        view.add(new JPanel(), constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        view.add(renderBottom(), constraints);

        registerEvents();
    }

    /**
     * Render the main part of the view
     * @return the main panel
     */
    private JPanel renderMain() {
        JPanel main = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(20, 20, 20, 20);

        // Title
        JLabel titleTxt = new JLabel("Available rooms");
        titleTxt.setForeground(ColorUtil.black);
        titleTxt.setFont(FontUtil.titleFont);
        main.add(titleTxt, constraints);

        // Room list
        constraints.gridy = 1;
        constraints.weighty = 10;
        roomContainer = new JPanel(new GridBagLayout());
        GridBagConstraints roomConstraints = new GridBagConstraints();
        roomConstraints.gridx = 0;
        roomConstraints.gridy = 0;
        roomConstraints.anchor = GridBagConstraints.NORTH;
        roomConstraints.weightx = 1;
        roomConstraints.weighty = 1;
        roomConstraints.gridheight = GridBagConstraints.REMAINDER;
        roomConstraints.gridwidth = GridBagConstraints.REMAINDER;
        roomContainer.add(new JPanel(), roomConstraints);

        JScrollPane scrollPane = new JScrollPane(roomContainer);
        main.add(scrollPane, constraints);

        // Create room title
        constraints.gridy = 2;
        constraints.weighty = 1;
        JLabel createTitle = new JLabel("Create a room");
        createTitle.setForeground(ColorUtil.black);
        createTitle.setFont(FontUtil.titleFont);
        main.add(createTitle, constraints);

        // Room name
        constraints.gridy = 3;
        roomNameIF = new JTextField();
        roomNameIF.setForeground(ColorUtil.black);
        roomNameIF.setFont(FontUtil.defaultFont);
        main.add(roomNameIF, constraints);

        // Create button
        constraints.gridy = 4;
        createRoomBTN = new JButton("Create");
        createRoomBTN.setBackground(ColorUtil.buttonBack);
        createRoomBTN.setForeground(ColorUtil.black);
        createRoomBTN.setFont(FontUtil.defaultFont);
        main.add(createRoomBTN, constraints);

        roomListConstraints = new GridBagConstraints();
        roomListConstraints.gridy = 1;
        roomListConstraints.gridx = 0;
        roomListConstraints.weightx = 1;
        roomListConstraints.insets = new Insets(10, 10, 10, 10);
        roomListConstraints.fill = GridBagConstraints.HORIZONTAL;
        roomListConstraints.anchor = GridBagConstraints.NORTH;


        return main;
    }

    /**
     * Render the bottom bar of the view
     * @return the bottom bar panel
     */
    private JPanel renderBottom() {
        JPanel bottomPanel = new JPanel(new GridBagLayout());

        backBTN = new JButton("Back to the chat");
        backBTN.setForeground(ColorUtil.black);
        backBTN.setBackground(ColorUtil.buttonBack);
        backBTN.setFont(FontUtil.defaultFont);

        bottomPanel.add(backBTN);

        return bottomPanel;
    }

    /**
     * Register events of the view to handle user actions
     */
    private void registerEvents() {
        createRoomBTN.addActionListener(actionEvent -> {
            chatController.createRoom(roomNameIF.getText());
            roomNameIF.setText("");
        });

        backBTN.addActionListener(actionEvent -> {
            if (chatController.getJoinedRooms().size() > 0) chatView.renderChatPanel();
        });
    }

    /**
     * Update room list when the user leave a room
     * @param roomName the room name the user left
     */
    private void onLeaveRoom(String roomName) {
        if (roomEntries.containsKey(roomName)) {
            backBTN.setEnabled(chatController.getJoinedRooms().size() > 0);
            JPanel panel = roomEntries.get(roomName);
            panel.removeAll();
            renderRoom(roomName, panel);
            panel.revalidate();
            panel.repaint();
        }
    }

    /**
     * Update room list when the user join a room
     * @param roomName the room name the user joined
     */
    private void onJoinRoom(String roomName) {
        if (roomEntries.containsKey(roomName)) {
            backBTN.setEnabled(chatController.getJoinedRooms().size() > 0);
            JPanel panel = roomEntries.get(roomName);
            panel.removeAll();
            renderRoom(roomName, panel);
            panel.revalidate();
            panel.repaint();
        }
    }

    /**
     * Render a room entry
     * @param roomName the room name
     * @param roomPanel the panel in which to render the room
     */
    private void renderRoom(String roomName, JPanel roomPanel) {
        roomPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;

        // Room name
        constraints.anchor = GridBagConstraints.WEST;
        JLabel nameTxt = new JLabel(roomName);
        nameTxt.setFont(FontUtil.defaultFont);
        nameTxt.setForeground(ColorUtil.black);
        roomPanel.add(nameTxt, constraints);

        // Room button
        JButton btn;
        if (chatController.getJoinedRooms().contains(roomName)) {
            btn = new JButton("Leave");
            btn.addActionListener(actionEvent -> {
                chatController.leaveRoom(roomName);
                onLeaveRoom(roomName);
            });
        } else {
            btn = new JButton("Join");
            btn.addActionListener(actionEvent -> {
                chatController.joinRoom(roomName);
                onJoinRoom(roomName);
            });
        }
        btn.setBackground(ColorUtil.buttonBack);
        btn.setForeground(ColorUtil.black);
        btn.setFont(FontUtil.defaultFont);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 1;
        roomPanel.add(btn, constraints);
    }

}
