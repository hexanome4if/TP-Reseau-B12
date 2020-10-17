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
import java.io.IOException;
import java.util.ArrayList;

public class ChatView {

    private final Font font = new Font("Arial", Font.PLAIN, 14);
    private final java.util.List<GlobalMessage> messages = new ArrayList<>();

    private final ChatController chatController;

    private JFrame frame;
    private JScrollPane chatList;
    private JPanel chatListPanel;
    private GridBagConstraints gridBagConstraints;
    private JTextField messageIF;
    private JButton sendMessageBTN;

    private JButton disconnectBTN;

    public ChatView() {
        chatController = new ChatController(this);
    }

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

    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }

    public void onReceiveMessage(GlobalMessage message) {
        SwingUtilities.invokeLater(() -> {
            messages.add(message);

            renderMessage(message);
        });
    }

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

    private  JPanel renderChat() {
        JPanel chat = new JPanel(new GridBagLayout());
        chat.setBackground(new Color(0, 255, 0));

        chatListPanel = new JPanel();
        chatListPanel.setBackground(new Color(0, 0, 255));
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

    private void registerEvents() {
        disconnectBTN.addActionListener(actionEvent -> chatController.disconnect());

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
