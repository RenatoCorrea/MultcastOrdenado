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
public class MessageId implements Comparable<MessageId> {
    private int clock;
    private int id;
    
    MessageId(int clock, int id) {
        this.clock = clock;
        this.id = id;
    }

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    // Sorting (For the priority queue)

    @Override
    public int compareTo(MessageId o) {
        int clockComp = Integer.compare(this.clock, o.clock);
        if (clockComp != 0)
            return clockComp;
        
        return Integer.compare(this.id, o.id);
    }
    
    // Hashing (For the HashMap)
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.clock;
        hash = 67 * hash + this.id;
        return hash;
    }
    
    // Equality

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MessageId other = (MessageId) obj;
        if (this.clock != other.clock) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}