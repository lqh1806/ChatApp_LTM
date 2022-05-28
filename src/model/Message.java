/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author lequo
 */
public class Message implements Serializable{
    private int idSend;
    private int idReceive;
    private String mess;
    private String createdTime;

    public Message(int idSend, int idReceive, String mess, String createdTime) {
        this.idSend = idSend;
        this.idReceive = idReceive;
        this.mess = mess;
        this.createdTime = createdTime;
    }

    public Message() {
    }

    public int getIdSend() {
        return idSend;
    }

    public void setIdSend(int idSend) {
        this.idSend = idSend;
    }

    public int getIdReceive() {
        return idReceive;
    }

    public void setIdReceive(int idReceive) {
        this.idReceive = idReceive;
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
    
    
}
