package stream.client.view;

import stream.client.controller.PseudoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class PseudoFrame {
    private PseudoController pseudoController;

    private JPanel panel1;
    private JTextField pseudoIF;
    private JButton setPseudoBTN;
    private JFrame frame;

    public PseudoFrame() {
        // pseudoController = new PseudoController(this);
    }

    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }

    public void show() {
        frame = new JFrame("INSAChat - Pseudo selection");

        frame.setContentPane(renderView());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setBackground(new Color(255, 255, 255));
        frame.setVisible(true);

        registerEvents();
    }

    private JPanel renderView() {
        JPanel view = new JPanel();

        // Global panel
        view.setBackground(new Color(255, 255, 255));
        view.setLayout(new BoxLayout(view, BoxLayout.PAGE_AXIS));

        // Title
        JLabel titleTxt = new JLabel("INSAChat");
        titleTxt.setForeground(new Color(0, 0,0));
        titleTxt.setFont(new Font("Arial", Font.PLAIN, 24));
        view.add(titleTxt);

        // Pseudo panel
        JPanel pseudoPanel = new JPanel();
        pseudoPanel.setLayout(new BoxLayout(pseudoPanel, BoxLayout.PAGE_AXIS));

        // Pseudo label
        JLabel pseudoLabel = new JLabel("Pseudo");
        pseudoLabel.setForeground(new Color(0, 0, 0));
        pseudoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        pseudoPanel.add(pseudoLabel);

        // Pseudo Input Field
        pseudoIF = new JTextField();
        pseudoIF.setForeground(new Color(0, 0, 0));
        pseudoIF.setFont(new Font("Arial", Font.PLAIN, 16));
        pseudoPanel.add(pseudoIF);

        view.add(pseudoPanel);

        // Submit button
        setPseudoBTN = new JButton("Connect");
        setPseudoBTN.setForeground(new Color(0, 0, 0));
        setPseudoBTN.setBackground(new Color(200, 200, 200));
        view.add(setPseudoBTN);

        return view;
    }

    private void registerEvents() {
        setPseudoBTN.addActionListener(actionEvent -> {
            setPseudoBTN.setEnabled(false);
            pseudoIF.setEnabled(false);
            pseudoController.sendPseudo(pseudoIF.getText());
        });
    }
}
