/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.MySQLConnect;
import DAO.RegisterDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import view.LoginView;
import view.RegisterView;


/**
 *
 * @author lequo
 */
public class RegisterController {
    private RegisterView rv;
    private byte[] filee;
    private RegisterDAO rd;

    public RegisterController() {
    }

    public RegisterController(RegisterView rv) throws SQLException {
        this.rv = rv;
        rd = new RegisterDAO(MySQLConnect.getConnection());
        rv.btn1AddListener(new chooseAva());
        rv.btn2AddListener(new register());
    }
    
    class chooseAva implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            rv.chooseFile();
            File file = rv.getFile();
            rv.setJLB8(file.getName());
            try {
                FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
                filee = new byte[(int)file.length()];
                fis.read(filee);
                rv.setAvatar(filee);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
        }     
    }
    
    class register implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = rv.getUsername();
            String password = rv.getPassword();
            String name = rv.getName();
            String phone = rv.getPhone();
            String email = rv.getEmail();
            if(username.equals("") || password.equals("") || name.equals("")|| phone.equals("") || email.equals("") || filee == null){
                rv.showAlert();
            }
            else{
                User u = new User(username, password, name, email, phone, filee);
                int rs = rd.register(u);
                if(rs == 1){
                    LoginView loginView = new LoginView();
                    try {
                        LoginController lc = new LoginController(loginView);
                    } catch (SQLException ex) {
                        Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    loginView.setVisible(true);
                    rv.exit();
                }
            }
        }
        
    }
}
