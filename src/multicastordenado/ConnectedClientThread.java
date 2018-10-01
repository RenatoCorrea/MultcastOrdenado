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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Renato Correa
 */
public class ConnectedClientThread extends Thread {
    public ConnectedClientThread(Socket socket, BufferedWriter[] writers) {
        this.socket = socket;
        this.writers = writers;
    }
    
    @Override
    public void run() {
        try {
            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(stream);

            while (this.isAlive()) {
                String message = reader.readLine();
                int clock = Integer.parseInt(reader.readLine()); // Receber tempo
                int id = Integer.parseInt(reader.readLine()); // Receber id
                int ack = Integer.parseInt(reader.readLine()); // Receber ack
                
                // Inserir o ack na estrutura
                // Se é a primeira mensagem e tem todos os acks
                    // "Imprimir" e remover da fila
                
                // Enviar ack
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectedClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private final Socket socket;
    private final BufferedWriter[] writers;
}
