/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastordenado;

import java.util.HashMap;
import java.util.PriorityQueue;

class MessageInfo {
    private final Message message;
    private int numAcks;

    public MessageInfo(MessageId messageId) {
        this.message = new Message(messageId);
        this.numAcks = 0;
    }

    public int getNumAcks() {
        return numAcks;
    }
    public void incrementNumAcks() {
        ++this.numAcks;
    }

    public Message getMessage() {
        return message;
    }
}

public class MessageQueue {
    private final HashMap<MessageId, MessageInfo> messageMap;
    private final PriorityQueue<MessageId> messageQueue;
    
    MessageQueue() {
        messageMap = new HashMap<>();
        messageQueue = new PriorityQueue<>();
    }
    
    private MessageInfo insertMessage(MessageId messageId) {
        MessageInfo messageInfo = new MessageInfo(messageId);
        
        messageMap.put(messageId, messageInfo);
        messageQueue.add(messageId);
        
        return messageInfo;
    }
    
    public synchronized void incrementMessageAcks(MessageId messageId) {
        MessageInfo messageInfo = messageMap.get(messageId);
        
        if (messageInfo == null)
            messageInfo = insertMessage(messageId);
        
        messageInfo.incrementNumAcks();
    }
    
    public synchronized void setMessageText(MessageId messageId, String text) {
        MessageInfo messageInfo = messageMap.get(messageId);
        
        if (messageInfo == null)
            messageInfo = insertMessage(messageId);
        
        messageInfo.getMessage().setText(text);
    }
    
    public synchronized Message tryDeliveringMessage() {
        if (messageQueue.isEmpty())
            return null;
        
        MessageId messageId = messageQueue.peek();
        MessageInfo messageInfo = messageMap.get(messageId);
        
        if (messageInfo.getMessage().getText() == null ||
            messageInfo.getNumAcks() < Node.NUM_NODES)
            return null;
        
        messageQueue.remove();
        messageMap.remove(messageId);
        
        return messageInfo.getMessage();
    }
}
