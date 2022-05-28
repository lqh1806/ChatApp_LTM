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
public class ListGroup implements Serializable{
    private User user;
    private List<Group> listGroup;
    private List<User> listUserDB;

    public List<Group> getListGroup() {
        return listGroup;
    }

    public void setListGroup(List<Group> listGroup) {
        this.listGroup = listGroup;
    }

    public List<User> getListUserDB() {
        return listUserDB;
    }

    public void setListUserDB(List<User> listUserDB) {
        this.listUserDB = listUserDB;
    }

    public ListGroup(List<Group> listGroup, List<User> listUserDB) {
        this.listGroup = listGroup;
        this.listUserDB = listUserDB;
    }

    public ListGroup(User user, List<Group> listGroup, List<User> listUserDB) {
        this.user = user;
        this.listGroup = listGroup;
        this.listUserDB = listUserDB;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    

    public ListGroup() {
    }
    
    
}
