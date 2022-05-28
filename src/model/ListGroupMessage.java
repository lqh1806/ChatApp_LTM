/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author lequo
 */
public class ListGroupMessage implements Serializable{
    private int idSend;
    private Group g;
    private List<Message> lm;

    public int getIdSend() {
        return idSend;
    }

    public void setIdSend(int idSend) {
        this.idSend = idSend;
    }

    public Group getG() {
        return g;
    }

    public void setG(Group g) {
        this.g = g;
    }

    public List<Message> getLm() {
        return lm;
    }

    public void setLm(List<Message> lm) {
        this.lm = lm;
    }

    
    
    public ListGroupMessage() {
    }

    public ListGroupMessage(int idSend, Group g) {
        this.idSend = idSend;
        this.g = g;
    }
    
    
}
