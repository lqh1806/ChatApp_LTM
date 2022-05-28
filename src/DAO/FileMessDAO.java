/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.FileMess;
import model.Message;
/**
 *
 * @author lequo
 */
public class FileMessDAO {
    private Connection conn;
    private Statement statement;
    private PreparedStatement preparedStatement;

    public FileMessDAO() {
    }

    public FileMessDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statement = this.conn.createStatement();
    }
    
    public int saveFileMess(FileMess fm){
        String sql = "INSERT INTO tbl_file (id_send, id_receive, file_data, file_name, extension, created_time) values (?,?,?,?,?,?)";
        int res = 0;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, fm.getIdSend());
            preparedStatement.setInt(2, fm.getIdReceive());
            preparedStatement.setBytes(3, fm.getFileData());
            preparedStatement.setString(4, fm.getFileName());
            preparedStatement.setString(5, fm.getExtension());
            preparedStatement.setString(6, fm.getCreatedTime());
            res = preparedStatement.executeUpdate();
        } catch (Exception e) {
        }
        return res;
    }
    
    public List<FileMess> getFileMess(int idSend, int idReceive){
        String sql = "";
        if(idReceive == -1){
            sql = "SELECT * FROM tbl_file where (id_receive = '" + idReceive + "')";
        }
        else sql = "SELECT * FROM tbl_file where (id_receive = '" + idReceive + "' and id_send = '" + idSend + "') or " + "(id_send = '" + idReceive + "' and id_receive = '" + idSend + "');";
        List<FileMess> list = new ArrayList<>();
        int i = 0;
        try {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                FileMess fm = new FileMess(rs.getInt(2), rs.getInt(3), rs.getBytes(4), rs.getString(5), rs.getString(6), rs.getString(7));
                list.add(fm);
            }
        } catch (Exception e) {
        }
        return list;
    }
}
