package stream.client.view;

import stream.client.controller.ChatController;
import stream.client.view.utils.ColorUtil;
import stream.client.view.utils.FontUtil;
import stream.core.GlobalMessage;
import stream.core.infos.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatPanel {

    /**
     * File extension considered as images
     */
    private final java.util.List<String> imagesExtensions = Arrays.asList("jpg", "jpeg", "png");

    /**
     * Default font to use
     */
    private final Font font = new Font("Arial", Font.PLAIN, 14);

    /**
     * Chat controller to handle user actions
     */
    private final ChatController chatController;

    /**
     * Actual chat view which has rendered this panel
     */
    private final ChatView chatView;

    /**
     * Window in which the panel is rendered
     */
    private final JFrame frame;

    /**
     * List of every messages
     */
    private final java.util.List<GlobalMessage> messages = new ArrayList<>();

    /**
     * The global container of the panel
     */
    private JPanel view;
    /**
     * List component containing the panel which contains the messages
     */
    private JScrollPane chatList;
    /**
     * Panel component containing the messages
     */
    private JPanel chatListPanel;
    /**
     * Constraints for the layout for the new messages
     */
    private GridBagConstraints gridBagConstraints;
    /**
     * Input field for user messages
     */
    private JTextField messageIF;
    /**
     * Send button
     */
    private JButton sendMessageBTN;
    /**
     * Send file message button
     */
    private JButton sendFileBTN;
    /**
     * Disconnect button
     */
    private JButton disconnectBTN;
    /**
     * Room management button
     */
    private JButton roomManagementBTN;
    /**
     * Room list panel component
     */
    private JPanel roomListPanel;

    /**
     * Room focused by user
     */
    private String currentRoom;

    /**
     * Create a new chat panel component to render
     * @param chatView the chat view managing the view
     * @param chatController the chat controller to handle user actions
     * @param frame the current window
     */
    public ChatPanel(ChatView chatView, ChatController chatController, JFrame frame) {
        this.chatView = chatView;
        this.chatController = chatController;
        this.frame = frame;
        createComponents();
    }

    /**
     * Render the panel and reload some components if needed
     * @return the global container panel
     */
    public JPanel render() {
        java.util.List<String> joinedRooms = chatController.getJoinedRooms();
        if (joinedRooms.size() == 0) {
            // Error here
            currentRoom = null;
        } else {
            if (currentRoom == null || !joinedRooms.contains(currentRoom)) {
                currentRoom = joinedRooms.get(0);
            }
        }

        // Update room list
        roomListPanel.removeAll();
        renderRoomList();
        roomListPanel.revalidate();
        roomListPanel.repaint();

        // Update chat
        chatListPanel.removeAll();
        GridBagConstraints spacerConstraints = new GridBagConstraints();
        spacerConstraints.gridx = 0;
        spacerConstraints.gridy = 0;
        spacerConstraints.weighty = 1;
        spacerConstraints.weightx = 1;
        spacerConstraints.gridwidth = GridBagConstraints.REMAINDER;
        spacerConstraints.gridheight = GridBagConstraints.REMAINDER;
        spacerConstraints.anchor = GridBagConstraints.NORTH;
        JPanel chatSpacer = new JPanel();
        chatListPanel.add(chatSpacer, spacerConstraints);
        gridBagConstraints.gridy = 1;
        for(GlobalMessage message: messages) {
            renderMessage(message);
        }

        return view;
    }

    /**
     * Save the new message and show it if it has been sent in the current user room
     * @param message the message received
     */
    public void onReceiveMessage(GlobalMessage message) {
        messages.add(message);
        renderMessage(message);
        chatListPanel.revalidate();
        chatListPanel.repaint();
    }

    /**
     * Initialize the different components of the panel
     */
    private void createComponents() {
        view = new JPanel(new GridBagLayout());
        view.setBackground(new Color(255, 255, 0));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        // Top bar
        view.add(renderTopBar(), constraints);

        // Chat
        constraints.gridy = 1;
        constraints.weighty = 10;
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints centerConstraints = new GridBagConstraints();
        centerConstraints.gridx = 0;
        centerConstraints.gridy = 0;
        centerConstraints.weighty = 1;
        centerConstraints.weightx = 1;
        centerConstraints.fill = GridBagConstraints.BOTH;
        roomListPanel = new JPanel();
        renderRoomList();
        centerPanel.add(roomListPanel, centerConstraints);
        centerConstraints.gridx = 1;
        centerConstraints.weightx = 8;
        centerPanel.add(renderChat(), centerConstraints);
        view.add(centerPanel, constraints);

        // Bottom bar
        constraints.gridy = 2;
        constraints.weighty = 1;
        view.add(renderBottomBar(), constraints);

        registerEvents();
    }

    /**
     * Render the top bar panel
     * @return the top bar panel component
     */
    private JPanel renderTopBar() {
        JPanel topBar = new JPanel();
        topBar.setLayout(new GridBagLayout());
        topBar.setBackground(new Color(255, 255, 255));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.insets = new Insets(0, 20, 0, 20);

        constraints.anchor = GridBagConstraints.WEST;
        roomManagementBTN = new JButton("Room management");
        roomManagementBTN.setForeground(new Color(0, 0, 0));
        roomManagementBTN.setBackground(new Color(200, 200, 200));
        roomManagementBTN.setFont(font);
        roomManagementBTN.setAlignmentX(Component.LEFT_ALIGNMENT);
        roomManagementBTN.setAlignmentY(Component.CENTER_ALIGNMENT);
        topBar.add(roomManagementBTN, constraints);

        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.EAST;

        disconnectBTN = new JButton("Disconnect");
        disconnectBTN.setForeground(new Color(0, 0, 0));
        disconnectBTN.setBackground(new Color(200, 200, 200));
        disconnectBTN.setFont(font);
        disconnectBTN.setAlignmentX(Component.RIGHT_ALIGNMENT);
        disconnectBTN.setAlignmentY(Component.CENTER_ALIGNMENT);

        topBar.add(disconnectBTN, constraints);
        return topBar;
    }

    /**
     * Render the chat panel
     * @return the chat panel component
     */
    private  JPanel renderChat() {
        JPanel chat = new JPanel(new GridBagLayout());
        chat.setBackground(new Color(255, 255, 255));

        chatListPanel = new JPanel();
        chatListPanel.setOpaque(false);
        chatListPanel.setLayout(new GridBagLayout());

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        chatList = new JScrollPane(chatListPanel);
        chat.add(chatList, constraints);


        return chat;
    }

    /**
     * Render the room list panel
     */
    private void renderRoomList() {
        roomListPanel.setLayout(new GridBagLayout());
        roomListPanel.setBackground(new Color(255, 0, 0));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;

        JPanel roomContainer = new JPanel(new GridBagLayout());

        java.util.List<String> joinedRooms = chatController.getJoinedRooms();

        GridBagConstraints spacerConstraints = new GridBagConstraints();
        spacerConstraints.gridx = 0;
        spacerConstraints.gridy = 0;
        spacerConstraints.weighty = 1;
        spacerConstraints.weightx = 1;
        spacerConstraints.gridwidth = GridBagConstraints.REMAINDER;
        spacerConstraints.gridheight = GridBagConstraints.REMAINDER;
        spacerConstraints.anchor = GridBagConstraints.NORTH;
        roomContainer.add(new JPanel(), spacerConstraints);

        GridBagConstraints roomConstraints = new GridBagConstraints();
        roomConstraints.gridx = 0;
        roomConstraints.gridy = 1;
        roomConstraints.weightx = 1;
        roomConstraints.fill = GridBagConstraints.HORIZONTAL;
        roomConstraints.anchor = GridBagConstraints.NORTH;

        for(String room: joinedRooms) {
            JPanel roomPanel = new JPanel(new GridBagLayout());
            roomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            if (room.equals(currentRoom)) {
                roomPanel.setBackground(new Color(200, 200, 200));
            }
            JLabel roomName = new JLabel(room);
            roomName.setForeground(ColorUtil.black);
            roomName.setFont(FontUtil.defaultFont);
            roomPanel.add(roomName);
            roomContainer.add(roomPanel, roomConstraints);

            roomPanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    currentRoom = room;
                    chatView.renderChatPanel();
                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {
                    if (currentRoom.equals(room)) return;
                    roomPanel.setBackground(new Color(150, 150, 150));
                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {
                    if (currentRoom.equals(room)) return;
                    roomPanel.setBackground(null);
                }
            });

            ++roomConstraints.gridy;
        }

        JScrollPane scroller = new JScrollPane(roomContainer);
        roomListPanel.add(scroller, constraints);
    }

    /**
     * Render the bottom bar panel
     * @return the bottom bar panel component
     */
    private JPanel renderBottomBar() {
        JPanel bottomBar = new JPanel(new GridBagLayout());
        bottomBar.setBackground(new Color(255, 255, 255));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weighty = 1;
        constraints.weightx = 10;
        constraints.gridx = 0;
        constraints.gridy = 0;

        messageIF = new JTextField();
        messageIF.setForeground(new Color(0, 0, 0));
        messageIF.setFont(font);
        messageIF.setPreferredSize(new Dimension(1000, 30));
        bottomBar.add(messageIF, constraints);

        constraints.weightx = 2;
        constraints.gridx = 1;
        sendFileBTN = new JButton("Send file");
        sendFileBTN.setForeground(new Color(0, 0, 0));
        sendFileBTN.setBackground(new Color(200, 200, 200));
        sendFileBTN.setFont(font);
        bottomBar.add(sendFileBTN, constraints);

        constraints.gridx = 2;
        sendMessageBTN = new JButton("Send");
        sendMessageBTN.setForeground(new Color(0, 0, 0));
        sendMessageBTN.setBackground(new Color(200, 200, 200));
        sendMessageBTN.setFont(font);
        bottomBar.add(sendMessageBTN, constraints);

        return bottomBar;
    }

    /**
     * Render a message received
     * @param message the message to render
     */
    private void renderMessage(GlobalMessage message) {
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new Color(255, 255, 255));
        messagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        switch (message.getType()) {
            case "message": {
                MessageInfo messageInfo = (MessageInfo)message.getData();
                if (!messageInfo.getRoomName().equals(currentRoom)) return;
                switch(messageInfo.getType()) {
                    case "text": {
                        TextMessageInfo textMessageInfo = (TextMessageInfo)messageInfo.getData();

                        messagePanel.setLayout(new GridBagLayout());

                        JPanel topContainer = new JPanel();
                        topContainer.setLayout(new GridBagLayout());
                        topContainer.setOpaque(false);

                        JLabel pseudoLabel = new JLabel(messageInfo.getUserPseudo());
                        pseudoLabel.setForeground(new Color(50, 50, 50));
                        pseudoLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                        JLabel dateLabel = new JLabel(message.getDate(), SwingConstants.RIGHT);
                        dateLabel.setForeground(new Color(50, 50, 50));
                        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                        GridBagConstraints constraints = new GridBagConstraints();
                        constraints.fill = GridBagConstraints.HORIZONTAL;
                        constraints.weightx = 1;
                        constraints.weighty = 1;
                        topContainer.add(pseudoLabel, constraints);
                        topContainer.add(dateLabel, constraints);

                        topContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

                        JLabel messageLabel = new JLabel(textMessageInfo.getContent());
                        messageLabel.setForeground(new Color(25, 25, 25));
                        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                        GridBagConstraints gridBagConstraints = new GridBagConstraints();
                        gridBagConstraints.weighty = 1;
                        gridBagConstraints.weightx = 1;
                        gridBagConstraints.gridy = 0;
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

                        messagePanel.add(topContainer, gridBagConstraints);
                        gridBagConstraints.gridy = 1;
                        messagePanel.add(messageLabel, gridBagConstraints);
                        break;
                    }
                    case "file": {
                        FileMessageInfo fileMessageInfo = (FileMessageInfo)messageInfo.getData();

                        messagePanel.setLayout(new GridBagLayout());

                        JPanel topContainer = new JPanel();
                        topContainer.setLayout(new GridBagLayout());
                        topContainer.setOpaque(false);

                        JLabel pseudoLabel = new JLabel(messageInfo.getUserPseudo());
                        pseudoLabel.setForeground(new Color(50, 50, 50));
                        pseudoLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                        JLabel dateLabel = new JLabel(message.getDate(), SwingConstants.RIGHT);
                        dateLabel.setForeground(new Color(50, 50, 50));
                        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                        GridBagConstraints constraints = new GridBagConstraints();
                        constraints.fill = GridBagConstraints.HORIZONTAL;
                        constraints.weightx = 1;
                        constraints.weighty = 1;
                        topContainer.add(pseudoLabel, constraints);
                        topContainer.add(dateLabel, constraints);

                        topContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

                        String[] fileNameSplit = fileMessageInfo.getFileName().split("\\.");
                        String fileExt = fileNameSplit[fileNameSplit.length - 1];
                        JLabel messageLabel;
                        if (imagesExtensions.contains(fileExt)) {
                            try {
                                BufferedImage image = ImageIO.read(new ByteArrayInputStream(fileMessageInfo.getFileContent()));
                                Image finalImage;
                                if (image.getWidth() > image.getHeight()) {
                                    finalImage = image.getScaledInstance(400, (image.getHeight() * 400) / image.getWidth(), 0);
                                } else {
                                    finalImage = image.getScaledInstance((image.getWidth() * 300) / image.getHeight(), 300, 0);
                                }
                                ImageIcon imageIcon = new ImageIcon(finalImage);
                                messageLabel = new JLabel(imageIcon);
                            } catch (IOException e) {
                                e.printStackTrace();
                                messageLabel = new JLabel("Cannot load image");
                            }
                        } else {
                            messageLabel = new JLabel("File");
                        }
                        messageLabel.setForeground(new Color(25, 25, 25));
                        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                        GridBagConstraints gridBagConstraints = new GridBagConstraints();
                        gridBagConstraints.weighty = 1;
                        gridBagConstraints.weightx = 1;
                        gridBagConstraints.gridy = 0;
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

                        messagePanel.add(topContainer, gridBagConstraints);
                        gridBagConstraints.gridy = 1;
                        gridBagConstraints.anchor = GridBagConstraints.EAST;
                        messagePanel.add(messageLabel, gridBagConstraints);
                        break;
                    }
                }
                break;
            }
            case "room-joined": {
                UserJoinedRoomInfo userJoinedRoomInfo = (UserJoinedRoomInfo)message.getData();
                if (!userJoinedRoomInfo.getRoomName().equals(currentRoom)) return;
                messagePanel.setLayout(new GridBagLayout());
                try {
                    Image image = ImageIO.read(new File("assets/green-arrow.png"));
                    image = image.getScaledInstance(15, 15, 0);
                    ImageIcon imageIcon = new ImageIcon(image);
                    JLabel icon = new JLabel(imageIcon);
                    messagePanel.add(icon);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                JLabel messageLabel = new JLabel(userJoinedRoomInfo.getPseudo() + " joined the chat!");
                messageLabel.setBorder(new EmptyBorder(0, 20, 0, 20));
                messageLabel.setForeground(new Color(6, 122, 37));
                messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));

                JLabel dateLabel = new JLabel(message.getDate(), SwingConstants.RIGHT);
                dateLabel.setForeground(new Color(50, 50, 50));
                dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                GridBagConstraints constraints = new GridBagConstraints();
                constraints.fill = GridBagConstraints.HORIZONTAL;
                constraints.weightx = 1;
                constraints.weighty = 1;

                messagePanel.add(messageLabel);
                messagePanel.add(dateLabel, constraints);
                break;
            }
            case "room-left": {
                UserLeftRoomInfo userLeftRoomInfo = (UserLeftRoomInfo)message.getData();
                if (!userLeftRoomInfo.getRoomName().equals(currentRoom)) return;
                messagePanel.setLayout(new GridBagLayout());
                try {
                    Image image = ImageIO.read(new File("assets/red-arrow.png"));
                    image = image.getScaledInstance(15, 15, 0);
                    ImageIcon imageIcon = new ImageIcon(image);
                    JLabel icon = new JLabel(imageIcon);
                    messagePanel.add(icon);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                JLabel messageLabel = new JLabel(userLeftRoomInfo.getPseudo() + " left the chat!");
                messageLabel.setBorder(new EmptyBorder(0, 20, 0, 20));
                messageLabel.setForeground(new Color(122, 21, 6));
                messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));

                JLabel dateLabel = new JLabel(message.getDate(), SwingConstants.RIGHT);
                dateLabel.setForeground(new Color(50, 50, 50));
                dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                GridBagConstraints constraints = new GridBagConstraints();
                constraints.fill = GridBagConstraints.HORIZONTAL;
                constraints.weightx = 1;
                constraints.weighty = 1;

                messagePanel.add(messageLabel);
                messagePanel.add(dateLabel, constraints);
                break;
            }
        }

        chatListPanel.add(messagePanel, gridBagConstraints);

        ++gridBagConstraints.gridy;
    }

    /**
     * Register user events on the view
     */
    private void registerEvents() {
        // Disconnect when user asks
        disconnectBTN.addActionListener(actionEvent -> chatController.disconnect());

        // Open room management panel on click
        roomManagementBTN.addActionListener(actionEvent -> chatView.renderRoomManagementPanel());

        // Send message on click on the send button
        sendMessageBTN.addActionListener(actionEvent -> {
            if (currentRoom == null) return;
            chatController.sendMessage(messageIF.getText(), currentRoom);
            messageIF.setText("");
        });

        // Send message when press enter
        sendMessageBTN.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "DoClick");
        sendMessageBTN.getActionMap().put("DoClick", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (currentRoom == null) return;
                chatController.sendMessage(messageIF.getText(), currentRoom);
                messageIF.setText("");
            }
        });
        messageIF.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "DoClick");
        messageIF.getActionMap().put("DoClick", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (currentRoom == null) return;
                chatController.sendMessage(messageIF.getText(), currentRoom);
                messageIF.setText("");
            }
        });

        // Send file messages
        sendFileBTN.addActionListener(actionEvent -> {
            if (currentRoom == null) return;
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                try {
                    chatController.sendFile(selectedFile, currentRoom);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        // Useful to scroll down when we add a new message
        chatListPanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                chatList.getVerticalScrollBar().setValue(chatList.getVerticalScrollBar().getMaximum());
            }
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e) {}
        });
    }
}
