/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

/**
 *
 * @author HoangHung
 */
public class Client implements Runnable {
     
    private DatagramSocket client;
    private String nickname;
    private JTextArea containMess;
    private JComboBox listUserOnline;
    private String allMess="";
    private ArrayList<String> AllUserOnline;


    public Client(String nickname, JTextArea containMess, JComboBox listUserOnline) {
        this.nickname = nickname;
        this.containMess = containMess;
        this.listUserOnline = listUserOnline;
    }
  
    
    // tạo luông luôn đọc dữ liệu từ server
    @Override
    public void run() {
        openClient();
        String message = "3;"+client.getLocalPort()+";"+nickname+";ALL MEMBER;"+nickname+ ": đã vào phòng \n";
        sentMess(message);
        while (true) {
            String packet=recieveMess();
            String[] messagerecive=packet.split(";",5);
            // bắt sự kiện tương ứng dược gửi về
            int event=Integer.parseInt(messagerecive[0]);
            
                   
            switch(event){
                    //nhận message từ sent mess to all
                    case 1:
                       allMess+=messagerecive[1];
                       containMess.setText(allMess);
                        break;
                    //sent mess to one one person
                    case 2:
                        allMess+=messagerecive[1];
                        containMess.setText(allMess);
                        break;
                        
                    // login system
                    case 3:
                        allMess+=messagerecive[2];
                        containMess.setText(allMess);
                        String listUsernow[]=messagerecive[1].split(":");
                        AllUserOnline=new ArrayList<>();
                        AllUserOnline.add("ALL MEMBER");
                        for(int i=0;i<listUsernow.length;i++){
                        if(nickname.endsWith(listUsernow[i])) continue;
                        AllUserOnline.add(listUsernow[i]);
                        }
                        listUserOnline.setModel(new DefaultComboBoxModel(AllUserOnline.toArray()));
                        break;
                    //logout system
                    case 4:
                        allMess+=messagerecive[2];
                        containMess.setText(allMess);
                        String listUserUpdate[]=messagerecive[1].split(":");
                        AllUserOnline=new ArrayList<>();
                        AllUserOnline.add("ALL MEMBER");
                        for(int i=0;i<listUserUpdate.length;i++){
                        if(nickname.endsWith(listUserUpdate[i])) continue;
                        AllUserOnline.add(listUserUpdate[i]);
                        }
                        listUserOnline.setModel(new DefaultComboBoxModel(AllUserOnline.toArray()));
                        break;
                }
        }
    }
    public void openClient() {
        try {
            client = new DatagramSocket();
            System.out.println(client.getLocalPort());
        } catch (SocketException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sentMess(String messagesent) {
        if (client != null) {
                try {
                    byte[] dataSent = new byte[1024];
                dataSent = messagesent.getBytes();
                DatagramPacket mess = new DatagramPacket(dataSent, dataSent.length, InetAddress.getByName("localhost"), 1234);
                    client.send(mess);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }

    public String recieveMess() {
        if (client != null) {
            try {
                byte[] dataRecieve = new byte[1024];
                DatagramPacket mess = new DatagramPacket(dataRecieve, dataRecieve.length);
                client.receive(mess);
                return new String(mess.getData());
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }
    public  int getLocalPortForPacket(){
        return client.getLocalPort();
    }
}
