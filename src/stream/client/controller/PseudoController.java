package stream.client.controller;

import stream.client.MainClient;
import stream.client.SenderThread;
import stream.client.view.ChatFrame;
import stream.client.view.PseudoFrame;
import stream.core.GlobalMessage;

public class PseudoController {

    private final PseudoFrame pseudoFrame;

    public PseudoController(PseudoFrame pseudoFrame) {
        this.pseudoFrame = pseudoFrame;
    }

    public void sendPseudo(String pseudo) {
        MainClient.send(new GlobalMessage(pseudo, "connect", null));
        new ChatFrame().show();
        pseudoFrame.close();
    }

}
