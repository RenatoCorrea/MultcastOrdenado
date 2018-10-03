/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastordenado;

import java.util.HashMap;
import java.util.PriorityQueue;

class MessageInfo {
    private String text;
    private int numAcks;

    public MessageInfo() {
        this.text = null;
        this.numAcks = 0;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumAcks() {
        return numAcks;
    }

    public void incrementNumAcks() {
        ++this.numAcks;
    }
}

public class MessageQueue {
    private final HashMap<MessageId, MessageInfo> messageMap;
    private final PriorityQueue<MessageId> messageQueue;
    
    MessageQueue() {
        messageMap = new HashMap<>();
        messageQueue = new PriorityQueue<>();
    }
    
    public synchronized void incrementMessageAcks(MessageId messageId) {
        MessageInfo messageInfo = messageMap.get(messageId);
        
        if (messageInfo == null) {
            messageInfo = new MessageInfo();
            messageMap.put(messageId, messageInfo);
            messageQueue.add(messageId);
        }
        
        messageInfo.incrementNumAcks();
    }
    
    public synchronized void setMessageText(MessageId messageId, String text) {
        MessageInfo messageInfo = messageMap.get(messageId);
        
        if (messageInfo == null) {
            messageInfo = new MessageInfo();
            messageMap.put(messageId, messageInfo);
            messageQueue.add(messageId);
        }
        
        messageInfo.setText(text);
    }
    
    public synchronized boolean isTopMessageReady() {
        MessageInfo messageInfo = messageMap.get(messageQueue.peek());
        
        return messageInfo.getText() != null &&
            messageInfo.getNumAcks() == Node.NUM_PROCESSES;
    }
    
    public synchronized String popMessage() {
        MessageId messageId = messageQueue.remove();
        MessageInfo messageInfo = messageMap.remove(messageId);
        return messageInfo.getText();
    }
}
