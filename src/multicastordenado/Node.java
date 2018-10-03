/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastordenado;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author Gustavo
 */
public class Node {
    public static final int NUM_PROCESSES = 3;
    
    // Node informations
    private int clock;
    private final int id;
    
    // Client socket writers
    private BufferedWriter[] writers;
    
    // Message queue
    private final MessageQueue messageQueue;
    
    public Node(int id) throws IOException {
        this.clock = 0;
        this.id = id;
        
        messageQueue = new MessageQueue();
    }
    
    public void initializeServer() {
        new ServerThread(this).start();
    }
    
    public void initializeClients() throws IOException {
        // Initialize client sockets
        Socket[] sockets = new Socket[NUM_PROCESSES];
        for (int i = 0; i < NUM_PROCESSES; ++i)
            sockets[i] = new Socket("localhost", 3031 + i);
        
        OutputStreamWriter[] streams = new OutputStreamWriter[NUM_PROCESSES];
        for (int i = 0; i < NUM_PROCESSES; ++i)
            streams[i] = new OutputStreamWriter(sockets[i].getOutputStream());
        
        writers = new BufferedWriter[NUM_PROCESSES];
        for (int i = 0; i < NUM_PROCESSES; ++i)
            writers[i] = new BufferedWriter(streams[i]);
    }
    
    public synchronized void multicastTextMessage(String text) throws IOException {
        for (int i = 0; i < NUM_PROCESSES; ++i) {
            // Write message
            writers[i].write(Integer.toString(clock) + "\n"); // Enviar o tempo
            writers[i].write(Integer.toString(id) + "\n"); // Enviar o id do remetente
            writers[i].write(Boolean.toString(false) + "\n"); // Enviar o ack
            writers[i].write(text + "\n"); // Enviar o texto

            // Flush output stream
            writers[i].flush();
        }
        
        // Increment clock
        ++clock;
    }
    
    public synchronized void multicastAckMessage(MessageId messageId) throws IOException {
        for (int i = 0; i < NUM_PROCESSES; ++i) {
            // Write message
            writers[i].write(messageId.getClock() + "\n"); // Enviar o tempo
            writers[i].write(messageId.getId() + "\n"); // Enviar o id do remetente
            writers[i].write(Boolean.toString(true) + "\n"); // Enviar o ack

            // Flush output stream
            writers[i].flush();
        }
        
        // Increment clock
        ++clock;
    }
    
    public void updateClock(int receivedClock) {
        clock = Integer.max(clock, receivedClock) + 1;
    }

    public int getId() {
        return id;
    }

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }
}
