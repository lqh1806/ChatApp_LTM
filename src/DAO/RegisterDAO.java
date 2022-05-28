/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import model.User;

/**
 *
 * @author lequo
 */
public class RegisterDAO {
    private Connection conn;
    private PreparedStatement ps;

    public RegisterDAO() {
    }

    public RegisterDAO(Connection conn) {
        this.conn = conn;
    }
    
    public int register(User user){
        String sql = "INSERT INTO TBL_USER (username, password, name, phone, email, avatar) values (?,?,?,?,?,?)";
        int res = 0;
        try {
            this.ps = this.conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getEmail());
            ps.setBytes(6, user.getAvatar());
            res = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
