package stream.client.view;

import stream.client.controller.PseudoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class PseudoFrame {
    private JPanel panel1;
    private JTextField pseudoIF;
    private JButton setPseudoBTN;
    private JFrame frame;

    public PseudoFrame() {
        PseudoController pseudoController = new PseudoController(this);
        setPseudoBTN.addActionListener(actionEvent -> {
            setPseudoBTN.setEnabled(false);
            pseudoIF.setEnabled(false);
            pseudoController.sendPseudo(pseudoIF.getText());
        });
    }

    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }

    public void show() {
        frame = new JFrame("INSAChat - Pseudo selection");

        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setBackground(new Color(255, 255, 255));
        frame.setVisible(true);

    }
}
