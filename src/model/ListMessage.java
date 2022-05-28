/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lequo
 */
public class ListMessage implements Serializable{
    private int idSend;
    private int idReceive;
    private List<Message> list;
    private List<FileMess> lm;
    private List<User> listUserDB;

    public ListMessage() {
    }

    public ListMessage(List<Message> list) {
        this.list = new ArrayList<>();
        this.list = list;
    }

    public ListMessage(int idSend, int idReceive, List<Message> list) {
        this.idSend = idSend;
        this.idReceive = idReceive;
        this.list = list;
    }

    public ListMessage(int idSend, int idReceive, List<Message> list, List<FileMess> lm, List<User> listUserDB) {
        this.idSend = idSend;
        this.idReceive = idReceive;
        this.list = list;
        this.lm = lm;
        this.listUserDB = listUserDB;
    }

    public List<FileMess> getLm() {
        return lm;
    }

    public void setLm(List<FileMess> lm) {
        this.lm = lm;
    }
    
    

    public List<Message> getList() {
        return list;
    }

    public void setList(List<Message> list) {
        this.list = list;
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

    public List<User> getListUserDB() {
        return listUserDB;
    }

    public void setListUserDB(List<User> listUserDB) {
        this.listUserDB = listUserDB;
    }
    
    
    
}
