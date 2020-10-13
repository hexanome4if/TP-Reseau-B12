/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.insa.chat.server;

import com.insa.chat.core.GlobalMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author polo
 */
public class ClientThread extends Thread {
    
    private Socket clientSocket;
    private ObjectInputStream reader;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    

    @Override
    public void run() {
        try {
            reader = new ObjectInputStream(clientSocket.getInputStream());
            
            while (true) {
                try {
                    GlobalMessage message = (GlobalMessage)reader.readObject();
                    handleMessage(message);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void handleMessage(GlobalMessage message) {
        switch (message.getType()) {
            case "message": {
                MainServer.
                break;
            }
        }
    }
    
}
