package stream.client.view;

import stream.client.controller.ChatController;
import stream.core.GlobalMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

public class ChatView {

    /**
     * Default font to use
     */
    private final Font font = new Font("Arial", Font.PLAIN, 14);

    /**
     * Reference to the chat controller to handle user actions
     */
    private final ChatController chatController;

    /**
     * Actual window
     */
    private JFrame frame;
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
     * Disconnect button
     */
    private JButton disconnectBTN;

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

        frame.setContentPane(renderView());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setBackground(new Color(255, 255, 255));
        frame.setVisible(true);

        registerEvents();

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
        // Important to be thread safe
        SwingUtilities.invokeLater(() -> {
            renderMessage(message);
        });
    }

    /**
     * Render the view with the different panels
     * @return the panel representing the entire view
     */
    private JPanel renderView() {
        JPanel view = new JPanel(new GridBagLayout());
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
        view.add(renderChat(), constraints);

        // Bottom bar
        constraints.gridy = 2;
        constraints.weighty = 1;
        view.add(renderBottomBar(), constraints);

        return view;
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
        gridBagConstraints.weighty = 1;
        gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        chatList = new JScrollPane(chatListPanel);
        chat.add(chatList, constraints);


        return chat;
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

        constraints.gridx = 1;
        constraints.weightx = 2;
        sendMessageBTN = new JButton("Send");
        sendMessageBTN.setForeground(new Color(0, 0, 0));
        sendMessageBTN.setBackground(new Color(200, 200, 200));
        sendMessageBTN.setFont(font);
        bottomBar.add(sendMessageBTN, constraints);

        return bottomBar;
    }

    /**
     * Register user events on the view
     */
    private void registerEvents() {
        // Disconnect when user asks
        disconnectBTN.addActionListener(actionEvent -> chatController.disconnect());

        // Send message on click on the send button
        sendMessageBTN.addActionListener(actionEvent -> {
            chatController.sendMessage(messageIF.getText());
            messageIF.setText("");
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
                messagePanel.setLayout(new GridBagLayout());

                JPanel topContainer = new JPanel();
                topContainer.setLayout(new GridBagLayout());
                topContainer.setOpaque(false);

                JLabel pseudoLabel = new JLabel(message.getPseudo());
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

                JLabel messageLabel = new JLabel(message.getData());
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
            case "connect": {
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
                JLabel messageLabel = new JLabel(message.getPseudo() + " joined the chat!");
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
            case "disconnect": {
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
                JLabel messageLabel = new JLabel(message.getPseudo() + " left the chat!");
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
        chatListPanel.revalidate();
        chatListPanel.repaint();

        ++gridBagConstraints.gridy;
    }
}
