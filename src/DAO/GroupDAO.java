/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.*;

/**
 *
 * @author lequo
 */
public class GroupDAO {
    private Connection conn;
    Statement statement;
    PreparedStatement preparedStatement;

    public GroupDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statement = conn.createStatement();
    }
    
    public int addGroup(Group g){
        String sql = "INSERT INTO tbl_group(name) values (?)";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, g.getName());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql2 = "SELECT MAX(ID) FROM tbl_group";
        int maxx = -1;
        try {
            ResultSet rs = statement.executeQuery(sql2);
            while(rs.next()){
                maxx = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql3 = "INSERT INTO tbl_group_user(id_group, id_user) values (?,?)";
        try { 
            List<Integer> list = new ArrayList<>();
            list = g.getListUser();
            for(int i = 0; i < list.size(); i++){
                preparedStatement = conn.prepareStatement(sql3);
                int id = list.get(i);
                preparedStatement.setInt(1, maxx);
                preparedStatement.setInt(2, id);
                System.out.println(id);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxx;
    }
    
    public List<Group> getGroup(int id){//lay danh sach group cua user
        String sql1 = "SELECT * FROM tbl_group_user where id_user = '" + id + "'";
        List<Group> list = new ArrayList<>();
        List<Integer> listIdgroup = new ArrayList<>();
        List<List<Integer>> listt = new ArrayList<>();
        try {
            ResultSet rs =statement.executeQuery(sql1);
            while(rs.next()){
                int id_group = rs.getInt(2);
                listIdgroup.add(id_group);
            }
            for(int i = 0; i < listIdgroup.size(); i++){
                int id_group = listIdgroup.get(i);
                String sql3 = "SELECT * FROM tbl_group_user where id_group = '" + id_group + "'";
                List<Integer> listuser = new ArrayList<>();
                rs = statement.executeQuery(sql3);
                while(rs.next()) {
                    int iduser = rs.getInt(3);
                    listuser.add(iduser);
                }
                listt.add(listuser);
            }
            for(int i = 0; i < listIdgroup.size(); i++){
                int id_group = listIdgroup.get(i);
                String sql2 = "SELECT * FROM tbl_group where id = '" + id_group +"'";
                rs = statement.executeQuery(sql2);
                String name = "";
                while(rs.next()){
                    name = rs.getString(2);
                    Group gg = new Group(id_group, name, listt.get(i));
                    list.add(gg);
                }    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    } 
    
    public int saveMessage(GroupMessage gm){
        String sql ="INSERT INTO TBL_MESSAGE_GROUP (ID_SEND, ID_GROUP, MESSAGE, CREATED_TIME) values (?,?,?,?)";
        int row = 0;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, gm.getIdSend());
            preparedStatement.setInt(2, gm.getG().getId());
            preparedStatement.setString(3, gm.getMess());
            preparedStatement.setString(4, gm.getCreatedTime());
            row = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }
    
    public List<Message> getHistoryGroupMessage(int idGroup){
        String sql = "SELECT * FROM TBL_MESSAGE_GROUP where id_group = '" + idGroup + "'";
        List<Message> lm = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                Message m = new Message(rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5));
                lm.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lm;
    }
}
