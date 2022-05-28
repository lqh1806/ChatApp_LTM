/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.User;

/**
 *
 * @author lequo
 */
public class LoginDAO {
    private Connection conn;
    Statement statement;

    public LoginDAO() {
    }

    public LoginDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statement = this.conn.createStatement();
    }
    
    public User checkLogin(String username, String password){
        String sql = "SELECT * FROM tbl_user where username = '" + username + "' and password = '" + password + "'";
        User user = new User();
        try {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6),rs.getBytes(7));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    
    public List<User> getAllUser(){
        String sql = "SELECT * FROM tbl_user";
        User user = new User();
        List<User> list = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6),rs.getBytes(7));
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
