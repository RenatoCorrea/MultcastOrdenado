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
        int clock = Integer.parseInt(bufferedReader.readLine());
        int nodeId = Integer.parseInt(bufferedReader.readLine());
        
        // Update node clock
        node.updateClock(clock);
        
        boolean isAck = Boolean.parseBoolean(bufferedReader.readLine());

        if (!isAck) {
            // Create message id
            MessageId messageId = new MessageId(clock, nodeId);
            
            // Receive and save message text
            String text = bufferedReader.readLine();
            node.getMessageQueue().setMessageText(messageId, text);

            // Send acks to all nodes
            node.multicastAckMessage(messageId);
        } else {
            // Acknowledged message informations
            int messageClock = Integer.parseInt(bufferedReader.readLine());
            int messageNodeId = Integer.parseInt(bufferedReader.readLine());
            
            // Create message id
            MessageId messageId = new MessageId(messageClock, messageNodeId);
            
            // Increment message acks
            node.getMessageQueue().incrementMessageAcks(messageId);
        }

        // Try to deliver the message
        Message message = node.getMessageQueue().tryDeliveringMessage();
        if (message == null)
            return;

        // Print the delivered message
        System.out.print("Mensagem de " + message.getId().getNodeId() + ": " + message.getText() + "\n");
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
