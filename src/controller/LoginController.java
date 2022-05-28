/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.LoginDAO;
import DAO.MySQLConnect;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ListGroup;
import model.User;
import view.LoginView;
import view.UserView;

/**
 *
 * @author lequo
 */
public class LoginController {
    private LoginView loginView;
    private LoginDAO ld;
    Object o;

    public LoginController(LoginView loginView, LoginDAO ld) {
        this.loginView = loginView;
        this.ld = ld;
    }

    public LoginController() {
    }

    public LoginController(LoginView loginView) throws SQLException {
        ld = new LoginDAO(MySQLConnect.getConnection());
        this.loginView = loginView;
        loginView.btn1addListener(new checkLogin());
    }
    
    class checkLogin implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if(loginView.getUsername().equals("") || loginView.getPassword().equals("")){
                    loginView.showAlert("Không được để trống thông tin");
                }
                else{
                    Socket socket = new Socket("localhost", 8888);
                    new UserThread(socket).start();
                }
            } catch (Exception ex) {
                
                ex.printStackTrace();
            }
        }
        
    }
    
    class UserThread extends Thread{
        private Socket socket;

        public UserThread() {
        }

        public UserThread(Socket socket) {
            this.socket = socket;
        }
        

        @Override
        public void run() {
            try {
                new ObjectOutputStream(socket.getOutputStream()).writeObject(new User(loginView.getUsername(), loginView.getPassword()));
                o = new ObjectInputStream(socket.getInputStream()).readObject();
                if (o instanceof ListGroup) { //thông tin đúng thì Server sẽ gửi lại ListGroup bao gồm các thông tin ban đầu cho User
                    ListGroup lg = (ListGroup) o;
                    User u = lg.getUser();
                    if (u.isIsCheck()) {
                        UserView userView = new UserView(u, socket);
                        UserController uc = new UserController(userView, socket, u, lg);
                        userView.setVisible(true);
                        loginView.exit();
                    }
                }
                else if(o instanceof User){//Vì thông tin sai nên server sẽ gửi lại User
                    loginView.showAlert("Sai thông tin đăng nhập và mật khẩu");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
}
