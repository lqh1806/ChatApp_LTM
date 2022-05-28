/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.LoginView;
import view.RegisterView;
import view.StartView;

/**
 *
 * @author lequo
 */
public class StartController {
    private StartView startView;

    public StartController() {
    }

    public StartController(StartView startView) {
        this.startView = startView;
        startView.btn1AddListener(new login());
        startView.btn2AddListener(new register());
    }
    
    class login implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LoginView loginView = new LoginView();
            try {
                LoginController lc = new LoginController(loginView);
            } catch (SQLException ex) {
                Logger.getLogger(StartController.class.getName()).log(Level.SEVERE, null, ex);
            }
            loginView.setVisible(true);
            startView.exit();
        }   
    }
    
    class register implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            RegisterView registerView = new RegisterView();
            try {
                RegisterController rc = new RegisterController(registerView);
            } catch (SQLException ex) {
                Logger.getLogger(StartController.class.getName()).log(Level.SEVERE, null, ex);
            }
            registerView.setVisible(true);
            startView.exit();
        }   
    }
}
