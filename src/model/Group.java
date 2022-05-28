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
public class Group implements Serializable{
    private int id;
    private String name;
    private List<Integer> listUser;

    public Group(String name, List<Integer> listUser) {
        this.name = name;
        this.listUser = listUser;
    }

    public Group(int id, String name, List<Integer> listUser) {
        this.id = id;
        this.name = name;
        this.listUser = listUser;
    }

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
    
    public Group() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getListUser() {
        return listUser;
    }

    public void setListUser(List<Integer> listUser) {
        this.listUser = listUser;
    }
    
    public Object[] toObjects(){
        return new Object[]{name};
    }
}
