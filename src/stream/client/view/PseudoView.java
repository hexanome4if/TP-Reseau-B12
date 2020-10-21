package stream.client.view;

import stream.client.controller.PseudoController;

import javax.swing.*;
import java.awt.*;

public class PseudoView {
    /**
     * Pseudo controller to handle user actions
     */
    private final PseudoController pseudoController;

    /**
     * Input field for user pseudo
     */
    private JTextField pseudoIF;
    /**
     * Button to connect to the server
     */
    private JButton setPseudoBTN;
    /**
     * The actual window
     */
    private JFrame frame;

    /**
     * Create a view to choose a pseudo but does not render it
     */
    public PseudoView() {
        pseudoController = new PseudoController(this);
    }

    /**
     * Close the window
     */
    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }

    /**
     * Render the view
     */
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

    /**
     * Render the entire view
     * @return the panel representing the view
     */
    private JPanel renderView() {
        JPanel view = new JPanel();

        // Global panel
        view.setBackground(new Color(255, 255, 255));
        view.setLayout(new BoxLayout(view, BoxLayout.PAGE_AXIS));
        view.setSize(1200, 700);
        Box box = new Box(BoxLayout.Y_AXIS);
        view.add(box);
        box.add(Box.createVerticalGlue());
        JPanel mainPanel = new JPanel();
        mainPanel.setMaximumSize(new Dimension(200, 200));
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new GridBagLayout());
        box.add(mainPanel);
        box.add(Box.createVerticalGlue());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        // Title
        JLabel titleTxt = new JLabel("INSAChat", SwingConstants.CENTER);
        titleTxt.setForeground(new Color(0, 0,0));
        titleTxt.setFont(new Font("Arial", Font.PLAIN, 24));
        mainPanel.add(titleTxt, constraints);

        // Pseudo label
        JLabel pseudoLabel = new JLabel("Pseudo", SwingUtilities.LEFT);
        pseudoLabel.setForeground(new Color(0, 0, 0));
        pseudoLabel.setBackground(new Color(0, 255, 0));
        pseudoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        constraints.gridy = 1;
        mainPanel.add(pseudoLabel, constraints);

        // Pseudo Input Field
        pseudoIF = new JTextField();
        pseudoIF.setForeground(new Color(0, 0, 0));
        pseudoIF.setFont(new Font("Arial", Font.PLAIN, 16));
        pseudoIF.setMaximumSize(new Dimension(200, 30));
        pseudoIF.setMinimumSize(new Dimension(200, 30));
        constraints.gridy = 2;
        mainPanel.add(pseudoIF, constraints);


        // Submit button
        setPseudoBTN = new JButton("Connect");
        setPseudoBTN.setForeground(new Color(0, 0, 0));
        setPseudoBTN.setBackground(new Color(200, 200, 200));
        constraints.gridy = 3;
        mainPanel.add(setPseudoBTN, constraints);

        return view;
    }

    /**
     * Register user events to handle them
     */
    private void registerEvents() {
        setPseudoBTN.addActionListener(actionEvent -> {
            setPseudoBTN.setEnabled(false);
            pseudoIF.setEnabled(false);
            pseudoController.sendPseudo(pseudoIF.getText());
        });
    }
}
