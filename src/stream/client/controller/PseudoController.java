package stream.client.controller;

import stream.client.MainClient;
import stream.client.view.ChatView;
import stream.client.view.PseudoView;
import stream.core.GlobalMessage;
import stream.core.requests.ConnectRequest;

public class PseudoController {

    /**
     * Reference to the view
     */
    private final PseudoView pseudoView;

    /**
     * Create a PseudoController to handle the connect the view and the server
     * @param pseudoView the pseudo view
     */
    public PseudoController(PseudoView pseudoView) {
        this.pseudoView = pseudoView;
    }

    /**
     * Send user pseudo to the server
     * @param pseudo user pseudo
     */
    public void sendPseudo(String pseudo) {
        MainClient.send(new GlobalMessage("connect", new ConnectRequest(pseudo)));
        ChatView chatView = new ChatView();
        chatView.show(); // Open the chat view
        chatView.renderRoomManagementPanel();

        pseudoView.close(); // Close the pseudo view
    }

}
