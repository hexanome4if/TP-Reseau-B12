package stream.client.view;

import stream.client.controller.ChatController;
import stream.core.GlobalMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private ChatController chatController;

    private java.util.List<GlobalMessage> messages;

    public ChatFrame() {

        messages = new ArrayList<>();
        chatController = new ChatController(this);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chatController.sendMessage(messageTextField.getText());
                messageTextField.setText("");
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chatController.disconnect();
            }
        });
    }

    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }

    public void onReceiveMessage(GlobalMessage message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Message received in view");
                messages.add(message);
                System.out.println(message.getData());
                renderMessage(message);
                // chatListPanel.repaint();
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
    }

    private void renderMessage(GlobalMessage message) {
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new Color(255, 255, 255));
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.PAGE_AXIS));
        JLabel pseudoLabel = new JLabel(message.getPseudo());
        pseudoLabel.setForeground(new Color(50, 50, 50));
        pseudoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messagePanel.add(pseudoLabel);
        JLabel messageLabel = new JLabel(message.getData());
        messageLabel.setForeground(new Color(25, 25, 25));
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messagePanel.add(messageLabel);
        messagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chatListPanel.add(messagePanel, gridBagConstraints);
        chatListPanel.revalidate();
        chatListPanel.repaint();
        ++gridBagConstraints.gridy;
    }
}
