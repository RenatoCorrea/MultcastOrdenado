/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package multicastordenado;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Renato Correa
 */
public class ServerConnectionThread extends Thread {
    private BufferedReader bufferedReader;
            
    public ServerConnectionThread(Socket socket, Node node) {
        this.socket = socket;
        this.node = node;
    }
    
    private void receiveMessage() throws IOException {
        // Receive message
        int clock = Integer.parseInt(bufferedReader.readLine()); // Receber tempo
        int id = Integer.parseInt(bufferedReader.readLine()); // Receber id
        boolean isAck = Boolean.parseBoolean(bufferedReader.readLine()); // Receber ack
        
        MessageId messageId = new MessageId(clock, id);
        node.updateClock(clock);

        if (!isAck) {
            // Receive and save message text
            String text = bufferedReader.readLine();
            node.getMessageQueue().setMessageText(messageId, text);

            // Send acks to all other nodes
            node.multicastAckMessage(messageId);
        } else {
            // Increment message acks
            node.getMessageQueue().incrementMessageAcks(messageId);
        }

        if (node.getMessageQueue().isTopMessageReady()) {
            String text = node.getMessageQueue().popMessage();
            
            synchronized (System.out) {
                System.out.print("Mensagem recebida: " + text);
            }
        }
    }
    
    @Override
    public void run() {
        try {
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(streamReader);
            
            while (this.isAlive()) {
                receiveMessage();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private final Socket socket;
    private final Node node;
}
