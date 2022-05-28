
import DAO.FileMessDAO;
import DAO.GroupDAO;
import DAO.LoginDAO;
import DAO.MessageDAO;
import DAO.MySQLConnect;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FileMess;
import model.Group;
import model.GroupMessage;
import model.ListGroup;
import model.ListGroupMessage;
import model.ListMessage;
import model.Message;
import model.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lequo
 */
public class Server extends javax.swing.JFrame {

    /**
     * Creates new form Server
     */
    ServerSocket serverSocket;
    Map<Integer, Socket> map;
    List<User> listUser;   
    LoginDAO ldao;
    public Server() throws SQLException {
        initComponents();
        ldao = new LoginDAO(MySQLConnect.getConnection());
        map = new HashMap<>();
        listUser = new ArrayList<>();
        ServerHandle serverHandle = new ServerHandle();
        serverHandle.start();
    }
    
    class ServerHandle extends Thread{

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                jTextArea1.append("Server is running\n");
                while(true){
                    Socket socket = serverSocket.accept();
                    System.out.println(socket.getInetAddress());
                    User user = (User) new ObjectInputStream(socket.getInputStream()).readObject();
                    user = ldao.checkLogin(user.getUsername(), user.getPassword());
                    if(user.getName() != null){
                        user.setIsCheck(true);
                        
                        //Gui danh sach nhom ma user o trong do
                        ListGroup lg = new ListGroup();
                        List<User> lu = new ArrayList<>();
                        lu = ldao.getAllUser(); // Lấy tất cả người dùng có trong database
                        List<Group> listGroups = new ArrayList<>();
                        GroupDAO gdao = new GroupDAO(MySQLConnect.getConnection());
                        listGroups = gdao.getGroup(user.getID()); //Lấy tất cả các nhóm mà có user trong đó
                        lg.setUser(user);
                        lg.setListGroup(listGroups);
                        lg.setListUserDB(lu);
                        new ObjectOutputStream(socket.getOutputStream()).writeObject(lg); //Gửi trả lại cho phía user
                        
                        if(!map.containsKey(user.getID())){
                            map.put(user.getID(), socket);
                            listUser.add(user);
                            Set<Integer> list = map.keySet();
                            for(Integer key: list){
                                new ObjectOutputStream(map.get(key).getOutputStream()).writeObject(listUser); //Gửi listUser online cho tất cả những user khác
                            }
                            jTextArea1.append(user.getName() + " entered\n");
                            new ObjectReceive(user, socket).start();
                        }
                    }
                    else{
                        new ObjectOutputStream(socket.getOutputStream()).writeObject(user);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    class ObjectReceive extends Thread{
        private User user;
        private Socket socket;

        public ObjectReceive() {
        }

        public ObjectReceive(User user, Socket socket) {
            this.user = user;
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try {
                while(true){
                    Object ss = new ObjectInputStream(socket.getInputStream()).readObject();
                    if (ss instanceof Integer) {
                        // Nhận thông tin rằng 1 user vừa thoát ra khỏi hệ thống
                        //Cần loại bỏ nó ra khỏi list những user đang online và map chứa socket của nó
                        if((Integer)ss == user.getID()){
                            map.remove((Integer)ss);
                            for(int i = 0; i < listUser.size(); i++){
                                if(user.getID() == listUser.get(i).getID()){
                                    listUser.remove(i);
                                    break;
                                }
                            }
                            Set<Integer> list = map.keySet();
                            for(Integer key: list){
                                new ObjectOutputStream(map.get(key).getOutputStream()).writeObject(listUser);
                            }
                            jTextArea1.append(user.getName() + " exited\n");
                            socket.close();
                            break;
                        }
                    }
                    else if(ss instanceof Message){
                        if(((Message) ss).getIdReceive() != -1){
                            Message m = (Message) ss;
                            jTextArea1.append(m.getIdSend() + " sent to " + m.getIdReceive() + " mess: " + m.getMess() + "\n");
                            Socket socketReceive = map.get(m.getIdReceive());
                            new ObjectOutputStream(socketReceive.getOutputStream()).writeObject(m);
                            MessageDAO md = new MessageDAO(MySQLConnect.getConnection());
                            int res = md.saveMessage(m);
                            if(res == 1){
                                jTextArea1.append("Saved message successfully \n");
                            }else jTextArea1.append("Saved message failed \n");
                        }
                        else{
                            Message m = (Message) ss;
                            jTextArea1.append(m.getIdSend() + " sent to all: " + m.getMess() + "\n");
                            Set<Integer> list = map.keySet();
                            for(Integer key: list){
                                if((int)key != user.getID()){
                                    new ObjectOutputStream(map.get(key).getOutputStream()).writeObject(m);
                                }  
                            }
                            MessageDAO md = new MessageDAO(MySQLConnect.getConnection());
                            int res = md.saveMessage(m);
                            if(res == 1){
                                jTextArea1.append("Saved message successfully \n");
                            }else jTextArea1.append("Saved message failed \n");
                        }
                    }
                    else if(ss instanceof ListMessage){ //lấy tất cả lịch sử của các loại chat
                        List<Message> list = new ArrayList<>();
                        List<User> lu = new ArrayList<>();
                        List<FileMess> fm = new ArrayList<>();
                        MessageDAO md = new MessageDAO(MySQLConnect.getConnection());
                        FileMessDAO fmd = new FileMessDAO(MySQLConnect.getConnection());
                        list = md.getMessage(((ListMessage) ss).getIdSend(), ((ListMessage) ss).getIdReceive());
                        lu = ldao.getAllUser();
                        fm = fmd.getFileMess(((ListMessage) ss).getIdSend(), ((ListMessage) ss).getIdReceive());
                        ((ListMessage) ss).setListUserDB(lu);
                        ((ListMessage) ss).setList(list);
                        ((ListMessage) ss).setLm(fm);
                        try {
                            new ObjectOutputStream(socket.getOutputStream()).writeObject(ss);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if(ss instanceof FileMess){
                        FileMess fm = (FileMess) ss;
                        if(fm.getIdReceive() != -1){
                            jTextArea1.append(fm.getIdSend() + " sent a file name: " + fm.getFileName() + ", to " + fm.getIdReceive() + "\n");
                            new ObjectOutputStream(map.get(fm.getIdReceive()).getOutputStream()).writeObject(fm);
                            FileMessDAO fmd = new FileMessDAO(MySQLConnect.getConnection());
                            int res = fmd.saveFileMess(fm); // lưu file vào csdl
                            if(res == 1){
                                jTextArea1.append("Saved file successfully \n");
                            }else jTextArea1.append("Saved file failed \n");
                        }
                        else{
                            jTextArea1.append(fm.getIdSend() + " sent a file name: " + fm.getFileName() + ", to all\n");
                            Set<Integer> list = map.keySet();
                            for(Integer key: list){
                                if((int)key != user.getID()){
                                    new ObjectOutputStream(map.get(key).getOutputStream()).writeObject(fm);
                                }  
                            }
                            FileMessDAO fmd = new FileMessDAO(MySQLConnect.getConnection());
                            int res = fmd.saveFileMess(fm);
                            if(res == 1){
                                jTextArea1.append("Saved file successfully \n");
                            }else jTextArea1.append("Saved file failed \n");
                        }   
                    }
                    else if(ss instanceof Group){
                        Group g = (Group) ss;
                        GroupDAO gdao = new GroupDAO(MySQLConnect.getConnection());
                        int idGroup = gdao.addGroup(g); //lưu group vừa tạo vào csdl
                        g.setId(idGroup);
                        List<Integer> tmp = new ArrayList<>();
                        tmp = g.getListUser();
                        for(int i = 0; i < tmp.size(); i++){
                            new ObjectOutputStream(map.get(tmp.get(i)).getOutputStream()).writeObject(g);
                        }                       
                    }
                    else if(ss instanceof GroupMessage){
                        GroupMessage gm = (GroupMessage) ss;
                        GroupDAO gdao = new GroupDAO(MySQLConnect.getConnection());
                        int row = gdao.saveMessage(gm); // lưu tin nhắn của group đó
                        if(row == 1) jTextArea1.append("Save group message successfully");
                        System.out.println(gm.getMess());
                        Group g = gm.getG();
                        List<Integer> luser = new ArrayList<>();
                        luser = g.getListUser();
                        for(int j = 0; j < luser.size(); j++){
                            if(luser.get(j) != gm.getIdSend()){ //cần khác id thằng vừa gửi không sẽ bị tắc luồng
                                for(int l = 0; l < listUser.size(); l++){
                                    if(luser.get(j) == listUser.get(l).getID()){ //và chỉ gửi cho những thằng trong group mà đang online thôi nhé
                                        new ObjectOutputStream(map.get(luser.get(j)).getOutputStream()).writeObject(gm);
                                        break;
                                    }
                                }
                            }
                            
                        }
                    }
                    else if(ss instanceof ListGroupMessage){
                        ListGroupMessage lgm = (ListGroupMessage) ss;
                        List<Message> lm = new ArrayList<>();
                        GroupDAO gdao = new GroupDAO(MySQLConnect.getConnection());
                        lm = gdao.getHistoryGroupMessage(lgm.getG().getId());
                        lgm.setLm(lm);
                        new ObjectOutputStream(socket.getOutputStream()).writeObject(lgm);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jScrollPane2.setViewportView(jList1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Server");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(160, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(218, 218, 218))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Server().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
