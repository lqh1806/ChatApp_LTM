/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Message;

/**
 *
 * @author lequo
 */
public class MessageDAO {
    private Connection conn;
    private PreparedStatement preparedStatement;
    private Statement statement;

    public MessageDAO() {
    }

    public MessageDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statement = conn.createStatement();
    }
    
    public int saveMessage(Message m){
        String sql = "INSERT INTO TBL_MESSAGE (ID_SEND, ID_RECEIVE, MESS, CREATED_TIME) VALUES (?,?,?,?)";
        int res = 0;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, m.getIdSend());
            preparedStatement.setInt(2, m.getIdReceive());
            preparedStatement.setString(3, m.getMess());
            preparedStatement.setString(4, m.getCreatedTime());   
            res = preparedStatement.executeUpdate();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
    public List<Message> getMessage(int idSend, int idReceive){
        String sql = "";
        if(idReceive == -1){
            sql = "SELECT * FROM tbl_message where (id_receive = '" + idReceive + "')";
        }
        else sql = "SELECT * FROM tbl_message where (id_receive = '" + idReceive + "' and id_send = '" + idSend + "') or " + "(id_send = '" + idReceive + "' and id_receive = '" + idSend + "');";
        List<Message> list = new ArrayList<>();
        int i = 0;
        try {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                Message m = new Message(rs.getInt(5), rs.getInt(2), rs.getString(3), rs.getString(4));
                list.add(m);
            }
        } catch (Exception e) {
        }
        return list;
    }
}
