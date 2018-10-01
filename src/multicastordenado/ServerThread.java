/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package multicastordenado;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Renato Correa
 */
public class ServerThread extends Thread {
    public ServerThread(int id) {
        this.id = id;
    }
    
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(3031 + this.id);
            
            while (this.isAlive()) {
                Socket clientSocket = serverSocket.accept();
                
                ConnectedClientThread clientThread = new ConnectedClientThread(clientSocket, this.writers);
                clientThread.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private final int id;
    public BufferedWriter[] writers;
}
