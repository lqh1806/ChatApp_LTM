/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
import view.UserView;

/**
 *
 * @author lequo
 */
public class UserController {
    private UserView userView;
    private User userReceive;
    private User userSend;
    private List<User> listUser; //list user online
    private Socket socket;
    private int mode;
    private File fileSend;
    private ListGroup lg;
    private Group groupSend;
    public UserController() {
    }

    //Hàm khởi tạo này không dùng nữa nhé bro, dùng hàm ở dưới
    public UserController(UserView userView, Socket socket, User userSend) {
        this.mode = 0; //chat single 0, chat all 1, chat group 2;
        this.userView = userView;
        this.socket = socket;
        this.userSend = userSend;
        this.userView.jtableAddClickListener(clickTable());
        this.userView.jlistAddClickListener(clickList());
        this.userView.jbtn1AddListener(new btnSendMess());
        this.userView.frameClosing(windoiwClosing());
        this.userView.jbtn4AddListener(new btnChatAll());
        this.userView.jbtn2AddListener(new btnChooseFile());
        this.userView.jbtn3AddListener(new btnSendFile());        
        this.userView.jbtn5AddListener(new btnCreateGroup());
        new ObjectReceive(socket, userSend).start();
        //new ListGroupSend(socket, userSend).start();
    }
    
    public UserController(UserView userView, Socket socket, User userSend, ListGroup lg) {
        this.mode = 0; //chat single 0, chat all 1, chat group 2;
        this.userView = userView;
        this.socket = socket;
        this.userSend = userSend;
        this.lg = lg;
        this.userView.table2AddRow(lg.getListGroup());
        this.userView.jtableAddClickListener(clickTable());
        this.userView.jtable2AddClickListener(clickTable2());
        this.userView.jlistAddClickListener(clickList());
        this.userView.jbtn1AddListener(new btnSendMess());
        this.userView.frameClosing(windoiwClosing());
        this.userView.jbtn4AddListener(new btnChatAll());
        this.userView.jbtn2AddListener(new btnChooseFile());
        this.userView.jbtn3AddListener(new btnSendFile());        
        this.userView.jbtn5AddListener(new btnCreateGroup());
        new ObjectReceive(socket, userSend).start();
    }
    
    public final MouseAdapter clickTable() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = userView.getRow();
                mode = 0;
                //listUser = userView.getListUser();
                userReceive = listUser.get(index);
                userView.clearTextArea();
                userView.clearJList();
                userView.jtable2ClearSelection();
                //Send listMessage để lấy lịch sử tin nhắn
                ListMessage listMessage = new ListMessage();
                listMessage.setIdSend(userSend.getID());
                listMessage.setIdReceive(userReceive.getID());
                try {
                    new ObjectOutputStream(socket.getOutputStream()).writeObject(listMessage); // lay lich su chat solo
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        };
    }
    
    public final MouseAdapter clickTable2() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = userView.getRowTable2();
                mode = 2;
                List<Group> listG = new ArrayList<>();
                listG = lg.getListGroup();
                groupSend = listG.get(index);
                userView.clearTextArea();
                userView.jtable1ClearSelection();
                //Lấy lịch sử chat của group
                ListGroupMessage lgm = new ListGroupMessage(userSend.getID(), groupSend);
                try {
                    new ObjectOutputStream(socket.getOutputStream()).writeObject(lgm);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        };
    }
    
    public final MouseAdapter clickList(){
        //Khi mình click vào tên file ở JList, thì nó sẽ hiện ảnh để preview
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FileMess fm = userView.jListGetFile();
                userView.showFile(fm);
            }
        };
    }
    
    public final WindowAdapter windoiwClosing(){
        return new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                if(userSend.getID() != 0){
                    try {
                        //Khi mà User thoát đi, thì nó sẽ gửi chính cái ID của nó, để cái Server nó sẽ remove cái ID của nó khỏi danh sách những User online ý
                        new ObjectOutputStream(socket.getOutputStream()).writeObject(userSend.getID());
                        socket.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }
            
        };
    }
    
    class btnSendMess implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(mode == 0){
                //Chat solo
                String mess = userView.getMessage();
                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDate = myDateObj.format(myFormatObj);
                Message message = new Message(userSend.getID(), userReceive.getID(), mess, formattedDate);
                userView.insertTextArea("You send to" + userReceive.getName() +  " message: " + mess);
                userView.insertTextArea("At: " + formattedDate);
                try {
                    new ObjectOutputStream(socket.getOutputStream()).writeObject(message);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            else if(mode == 1){
                //Chat all
                String mess = userView.getMessage();
                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDate = myDateObj.format(myFormatObj);
                Message message = new Message(userSend.getID(), -1, mess, formattedDate); //idReceive -1 là send to all
                userView.insertTextArea("You send to all message: " + mess);
                userView.insertTextArea("At: " + formattedDate);
                try {
                    new ObjectOutputStream(socket.getOutputStream()).writeObject(message);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            else if(mode == 2){
                //Chat group
                String mess = userView.getMessage();
                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDate = myDateObj.format(myFormatObj);
                GroupMessage gm = new GroupMessage(userSend.getID(), groupSend, mess, formattedDate);
                userView.insertTextArea("You send to " + groupSend.getName() + ": " + mess);
                userView.insertTextArea("At: " + formattedDate);
                try {
                    new ObjectOutputStream(socket.getOutputStream()).writeObject(gm);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        }      
    }
    
    class btnChooseFile implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            userView.showFileChooser();
            fileSend = userView.getFileSend();
        }
        
    }
    
    class btnSendFile implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(userReceive != null){
                try {
                    FileInputStream fis = new FileInputStream(fileSend.getAbsoluteFile()); //Dùng luồng đọc File này để đọc File nhờ vào được dẫn tuyệt đối lấy từ cái File mà JFileChooser
                    byte[] fileData = new byte[(int)fileSend.length()]; //Tạo ra mảng byte để chứa cái file mà tí nữa luồng File nó đọc
                    fis.read(fileData); //Đọc cái File từ cái đường dẫn FileSend rồi chứa vào mảng byte kia
                    int index = fileSend.getName().lastIndexOf("."); //lấy vị trí dấu . để còn lấy định dạng File
                    String extension = fileSend.getName().substring(index + 1);
                    LocalDateTime myDateObj = LocalDateTime.now();
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String formattedDate = myDateObj.format(myFormatObj);
                    FileMess fm = null;
                    if(mode == 1){
                        //Gửi file cho tất cả
                        fm = new FileMess(userSend.getID(), -1, fileData, fileSend.getName(), extension, formattedDate);
                        userView.insertTextArea("You send a file to all " +  " message: " + fileSend.getName());
                        userView.insertTextArea("At: " + formattedDate);
                        userView.jListAddFile(fm);
                    }
                    else if(mode == 0){
                        //Gửi file cho 1 ngừoi
                        fm = new FileMess(userSend.getID(), userReceive.getID(), fileData, fileSend.getName(), extension, formattedDate);
                        userView.insertTextArea("You send a file to" + userReceive.getName() +  " message: " + fileSend.getName());
                        userView.insertTextArea("At: " + formattedDate);
                        userView.jListAddFile(fm);
                    }
                    new ObjectOutputStream(socket.getOutputStream()).writeObject(fm);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        }
        
    }
    
    class btnChatAll implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            mode = 1;
            //Cái ListMessage này khi gửi all thì cái IdReceive nó sẽ là -1 nhé, còn gửi solo thì nó là id của thằng nhận
            ListMessage listMessage = new ListMessage();
            listMessage.setIdSend(userSend.getID());
            listMessage.setIdReceive(-1);
            userView.clearSelectTable();// Xóa mấy cái table row khi ae chọn để chat solo ý
            userView.clearTextArea(); //Xóa cái text area đi 
            userView.clearJList(); //Xóa danh sách File
            try {
                new ObjectOutputStream(socket.getOutputStream()).writeObject(listMessage); //lay lich su chat all
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        } 
    }
    
    class btnCreateGroup implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int[] a = userView.getRows(); //Lấy tất cả vị trí những thằng nào mà được chọn ở Table user ý
            List<Integer> listtmp = new ArrayList<>(); 
            for(int i = 0; i < a.length; i++){
                //Xong từ các vị trí của nó trong table, mình ánh xạ sang cái list user online, để nhét nó vào cái List, để chứa các id của User mà mình muốn tạo group
                listtmp.add(listUser.get(a[i]).getID());
            }
            listtmp.add(userSend.getID()); //Lấy id của chính thằng đang tạo group nữa
            String name = userView.getGroupName(); //Lấy tên group
            Group group = new Group(name, listtmp);
            try {
                new ObjectOutputStream(socket.getOutputStream()).writeObject(group); //gửi group đến server để đăng ký
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }
    

    class ObjectReceive extends Thread{
        private Socket socket;
        private User user;

        public ObjectReceive(Socket socket, User user) {
            this.socket = socket;
            this.user = user;
        }

        public ObjectReceive() {
        }

        @Override
        public void run() {   
            try {
                while(true){
                    Object o = new ObjectInputStream(this.socket.getInputStream()).readObject();
                    if(o instanceof List){
                        //lấy danh sách những User đang online
                        listUser = (List<User>) o;
                        for(int i = 0; i < listUser.size(); i++){
                            if(user.getID() == listUser.get(i).getID()){
                                listUser.remove(i);
                                break;
                            }
                        }
                        userView.tableAddRow(listUser); //table mà hiển thị user
                    }
                    else if(o instanceof Message){         
                        if(((Message) o).getIdReceive() != -1){  //nếu mà là tin nhắn solo thì id nó sẽ khác -1
                            //nhận tin nhắn từ 1 người
                            User tmp = new User();
                            for(int i = 0; i < listUser.size(); i++){
                                if(((Message) o).getIdSend() == listUser.get(i).getID()){
                                    tmp = listUser.get(i); // Tìm đích danh người gửi tin nhắn này
                                    break;
                                }
                            }
                            userView.insertTextArea(tmp.getName() + " sent to you: " + ((Message) o).getMess() + "\nAt: " + ((Message) o).getCreatedTime());
                        }
                        else {
                            //Nhận tin nhắn từ 1 người mà chat đến cho tất cả mọi người
                            User tmp = new User();
                            for(int i = 0; i < listUser.size(); i++){
                                if(((Message) o).getIdSend() == listUser.get(i).getID()){
                                    tmp = listUser.get(i);// Tìm đích danh người gửi tin nhắn này
                                    break;
                                }
                            }
                            userView.insertTextArea(tmp.getName() + " sent to all: " + ((Message) o).getMess() + "\nAt: " + ((Message) o).getCreatedTime());
                        }
                    }
                    else if(o instanceof ListMessage){
                        List<Message> list = ((ListMessage) o).getList();
                        List<User> lu = ((ListMessage) o).getListUserDB(); //lấy tất cả user database (tại vì nếu mà là chat all, mà không lấy được tất cả user ở trong db, thì khi hiện lịch sử tin nhắn sẽ có những đứa không onl, gây ra tình trạng null)
                        List<FileMess> fm = ((ListMessage) o).getLm(); //lay lịch sử gửi file 
                        for(int i = 0; i < list.size(); i++){
                            Message m = list.get(i);
                            FileMess fmm = new FileMess();
                            if(i < fm.size()){
                                fmm = fm.get(i);
                            }
                            int idSend = m.getIdSend();
                            int idReceive = m.getIdReceive();
                            if(idReceive == -1){
                                //lich su chat all
                                User tmp = new User();
                                for(int j = 0; j < lu.size(); j++){
                                    if(idSend == lu.get(j).getID()){
                                        tmp = lu.get(j);
                                        break;
                                    }
                                }
                                userView.insertTextArea(tmp.getName() + " sent to all: " + m.getMess() + "\nAt: " + m.getCreatedTime());
                                if(fmm.getFileName() != null) userView.jListAddFile(fmm);
                            }
                            else{
                                //lich su chat solo
                                if(idSend == userSend.getID()){
                                    userView.insertTextArea("You send to" + userReceive.getName() +  " message: " + m.getMess());
                                    userView.insertTextArea("At: " + m.getCreatedTime());
                                    if(fmm.getFileName() != null){
                                        userView.insertTextArea("You send a file to " + userReceive.getName() + " name: " + fmm.getFileName());
                                        userView.insertTextArea("At: " + fmm.getCreatedTime());
                                        userView.jListAddFile(fmm);
                                    }
                                }
                                else if(idSend == userReceive.getID()){
                                    User tmp = new User();
                                    for(int j = 0; j < listUser.size(); j++){
                                        if(idSend == listUser.get(j).getID()){
                                            tmp = listUser.get(j);
                                            break;
                                        }
                                    }
                                    userView.insertTextArea(tmp.getName() + " sent to you: " + m.getMess() + "\nAt: " + m.getCreatedTime());
                                    if(fmm.getFileName() != null){
                                        userView.insertTextArea(tmp.getName() + " sent to you a file: " + fmm.getFileName() + "\nAt: " + fmm.getCreatedTime());
                                        userView.jListAddFile(fmm);
                                    }
                                }
                            }
                        }
                    }
                    else if(o instanceof FileMess){ //Nhận những file khi được gửi đến
                        FileMess fm = (FileMess) o;
                        userView.jListAddFile(fm);
                        if(fm.getIdReceive() == user.getID()){
                            User tmp = null;
                            for(int i = 0; i < listUser.size(); i++){
                                if(listUser.get(i).getID() == fm.getIdSend()){
                                    tmp = listUser.get(i);
                                    break;
                                }
                            }
                            userView.insertTextArea(tmp.getName() + " sent a file to you: " + fm.getFileName());
                        }
                        else{
                            User tmp = null;
                            for(int i = 0; i < listUser.size(); i++){
                                if(listUser.get(i).getID() == fm.getIdSend()){
                                    tmp = listUser.get(i);
                                    break;
                                }
                            }
                            userView.insertTextArea(tmp.getName() + " sent a file to all: " + fm.getFileName());
                        }
                    }
                    else if(o instanceof Group){ //Khi mà group được đăng ký trên server xong, nó sẽ gửi group này đến những thành viên có trong group đó để thông báo
                        Group g = (Group) o;
                        lg.getListGroup().add(g);
                        userView.table2AddRow(lg.getListGroup());
                    }
                    else if(o instanceof GroupMessage){ //Nhận tin nhắn từ group
                        GroupMessage gm = (GroupMessage) o;
                        List<Group> listGroup = lg.getListGroup(); //cái dòng này hình như là thừa bro nhé, nhưng t chưa dám xóa :v
                        User tmp = null;
                        Group g = gm.getG();
                        for (int i = 0; i < listUser.size(); i++) {
                            if(listUser.get(i).getID() == gm.getIdSend()){
                                tmp = listUser.get(i);
                                break;
                            }
                        }
                        userView.insertTextArea(tmp.getName() + " sent a message to " + g.getName() + ": " + gm.getMess());
                        userView.insertTextArea("At: " + gm.getCreatedTime());
                    }
                    else if(o instanceof ListGroupMessage){ //Lấy lịch sử tin nhắn của 1 group cụ thể
                        ListGroupMessage lgm = (ListGroupMessage) o;
                        List<User> listUserDB = lg.getListUserDB();
                        List<Message> lm = lgm.getLm();
                        User tmp = new User();
                        for (int i = 0; i < lm.size(); i++) {
                            Message m = lm.get(i);
                            for(int j = 0; j < listUserDB.size(); j++){
                                if(listUserDB.get(j).getID() == m.getIdSend()){
                                    userView.insertTextArea(listUserDB.get(j).getName() + " sent a message to group: " + m.getMess());
                                    userView.insertTextArea("At: " + m.getCreatedTime());
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }    
    }
}
