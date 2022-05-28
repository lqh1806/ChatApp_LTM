package DAO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;

/**
 *
 * @author lequo
 */
public class MySQLConnect {
    public static Connection getConnection() throws SQLException{
        String url = "jdbc:mysql://localhost:3306/" + "chatapp" +"?zeroDateTimeBehavior=convertToNull";
        Connection con = DriverManager.getConnection(url, "root", "");
        return con;
    }
}
