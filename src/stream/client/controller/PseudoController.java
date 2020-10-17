package stream.client.controller;

import stream.client.MainClient;
import stream.client.view.ChatFrame;
import stream.client.view.PseudoView;
import stream.core.GlobalMessage;

public class PseudoController {

    private final PseudoView pseudoView;

    public PseudoController(PseudoView pseudoView) {
        this.pseudoView = pseudoView;
    }

    public void sendPseudo(String pseudo) {
        MainClient.send(new GlobalMessage(pseudo, "connect", null));
        new ChatFrame().show();
        pseudoView.close();
    }

}
