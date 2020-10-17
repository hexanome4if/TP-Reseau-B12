package stream.client.view;

import stream.client.controller.ChatController;
import stream.core.GlobalMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ChatFrame {
    private JPanel panel1;
    private JButton sendButton;
    private JTextField messageTextField;
    private JButton button1;
    private JScrollPane messageList;
    private JFrame frame;
    private JPanel chatListPanel;
    private GridBagConstraints gridBagConstraints;

    private final ChatController chatController;

    private final java.util.List<GlobalMessage> messages;

    public ChatFrame() {

        messages = new ArrayList<>();
        chatController = new ChatController(this);
        sendButton.addActionListener(actionEvent -> {
            chatController.sendMessage(messageTextField.getText());
            messageTextField.setText("");
        });
        button1.addActionListener(actionEvent -> chatController.disconnect());
    }

    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }

    public void onReceiveMessage(GlobalMessage message) {
        SwingUtilities.invokeLater(() -> {
            messages.add(message);
            try {
                renderMessage(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void show () {
        frame = new JFrame("INSAChat - Chat");

        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setBackground(new Color(255, 255, 255));
        frame.setVisible(true);

    }

    private void renderMessageList() {
        chatListPanel.setLayout(new GridBagLayout());

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
    }

    private void createUIComponents() {
        chatListPanel = new JPanel();
        renderMessageList();
        messageList = new JScrollPane(chatListPanel);

        // Useful to scroll down when we add a new message
        chatListPanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                messageList.getVerticalScrollBar().setValue(messageList.getVerticalScrollBar().getMaximum());
            }
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e) {}
        });
    }

    private void renderMessage(GlobalMessage message) throws IOException {
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
                System.out.println("Disconnect message");
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
