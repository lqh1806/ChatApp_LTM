
import controller.StartController;
import view.StartView;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lequo
 */
public class Main {
    public static void main(String[] args) {
        StartView startView = new StartView();
        StartController startController = new StartController(startView);
        startView.setVisible(true);
    }
}
