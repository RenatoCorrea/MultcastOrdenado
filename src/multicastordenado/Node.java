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
    public static final int NUM_NODES = 3;
    
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
        Socket[] sockets = new Socket[NUM_NODES];
        for (int i = 0; i < NUM_NODES; ++i)
            sockets[i] = new Socket("localhost", 3031 + i);
        
        OutputStreamWriter[] streams = new OutputStreamWriter[NUM_NODES];
        for (int i = 0; i < NUM_NODES; ++i)
            streams[i] = new OutputStreamWriter(sockets[i].getOutputStream());
        
        writers = new BufferedWriter[NUM_NODES];
        for (int i = 0; i < NUM_NODES; ++i)
            writers[i] = new BufferedWriter(streams[i]);
    }
    
    public void multicastTextMessage(String text) throws IOException {
        for (int i = 0; i < NUM_NODES; ++i) {
            // Write message
            // - Message identification
            writers[i].write(Integer.toString(clock) + "\n"); // Clock
            writers[i].write(Integer.toString(id) + "\n"); // Node id
            // - Message ack flag
            writers[i].write(Boolean.toString(false) + "\n"); // Ack flag
            // - Message text
            writers[i].write(text + "\n"); // Message text

            // Flush output stream
            writers[i].flush();
        }
        
        // Increment clock
        ++clock;
    }
    
    public synchronized void multicastAckMessage(MessageId messageId) throws IOException {
        for (int i = 0; i < NUM_NODES; ++i) {
            // Write message
            // - Message identification
            writers[i].write(Integer.toString(clock) + "\n"); // Clock
            writers[i].write(Integer.toString(id) + "\n"); // Node id
            // - Message ack flag
            writers[i].write(Boolean.toString(true) + "\n"); // Ack flag
            // - Acked message identification
            writers[i].write(messageId.getClock() + "\n"); // Message clock
            writers[i].write(messageId.getNodeId() + "\n"); // Message node id

            // Flush output stream
            writers[i].flush();
        }
        
        // Increment clock
        ++clock;
    }
    
    public synchronized void updateClock(int receivedClock) {
        clock = Integer.max(clock, receivedClock) + 1;
    }

    public int getId() {
        return id;
    }

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }
}
