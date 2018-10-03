/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastordenado;

/**
 *
 * @author Gustavo
 */
public class Message {
    private MessageId id;
    private String text;
    
    Message(MessageId id) {
        this.id = id;
        this.text = null;
    }

    public MessageId getId() {
        return id;
    }
    
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
