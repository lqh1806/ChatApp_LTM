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
public class User implements Serializable{
    private int ID;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private byte[] avatar;
    private boolean isCheck;

    public User() {
    }

    public User(int ID, String username, String password, String name, String email, String phone, byte[] avatar) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
    }
    
    public User(String username, String password, String name, String email, String phone, byte[] avatar) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        isCheck = false;
        this.ID = 0;
    }
    
    public User(String username, String password){
        this.username = username;
        this.password = password;
        isCheck = false;
        this.ID = 0;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public boolean isIsCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
    
    
}
