/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.insa.chat.server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author polo
 */
public class MainServer {
    
    public static void main(String[] args) {
        if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
          System.exit(1);
  	}
        
        startServer(Integer.parseInt(args[0]));
    }
    
    private static void startServer(int port) {
        ServerSocket listenSocket;
        
  	
	try {
		listenSocket = new ServerSocket(port); //port
		System.out.println("Server ready..."); 
		while (true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("Connexion from:" + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
			ClientThread ct = new ClientThread(clientSocket);
			ct.start();
		}
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }
    
    public static synchronized void broadcastMessage(String message, Socket socket) {
        
    }
}
