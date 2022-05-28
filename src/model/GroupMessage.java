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
public class GroupMessage implements Serializable{
    private int idSend;
    private Group g;
    private String mess;
    private String createdTime;

    public GroupMessage(int idSend, int idGroup, String mess, String createdTime) {
        this.idSend = idSend;
        this.mess = mess;
        this.createdTime = createdTime;
    }

    public GroupMessage(int idSend, Group g, String mess, String createdTime) {
        this.idSend = idSend;
        this.g = g;
        this.mess = mess;
        this.createdTime = createdTime;
    }

    public GroupMessage() {
    }

    public int getIdSend() {
        return idSend;
    }

    public void setIdSend(int idSend) {
        this.idSend = idSend;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Group getG() {
        return g;
    }

    public void setG(Group g) {
        this.g = g;
    }
    
    
}
