/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HoangHung
 */
public class ServerStart implements Runnable{

    private int port = 1234;
    private DatagramSocket server;
    private Hashtable<Integer,String> listUserConnect = new Hashtable<>();

    public void openServer() {
        try {
            server = new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(ServerStart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void StartServer() {
            while (true) {
                DatagramPacket dataRecieve=recieve();
                String packet=new String(dataRecieve.getData());
                String message[]=packet.split(";",5);
                // thông điệp được tổ chức sự kiên;port_from;nickname,nicknamesent;message
                int event=Integer.parseInt(message[0]);
                switch(event){
                    //sent message to all
                    case 1:
                        sendMessageToAll("1;"+message[4]);
                        break;
                    //sent mess to one one person
                    case 2:
                        sendMess("2;"+message[4],dataRecieve.getPort());
                        Set<Integer> keySet=listUserConnect.keySet();
                        for(Integer key:keySet){
                        if(listUserConnect.get(key).endsWith(message[3])){
                            sendMess("2;"+message[4],key);
                            break;
                        }
                        }
                        break;
                    // login system
                    case 3:
                        listUserConnect.put(Integer.parseInt(message[1]),message[2]);
                        Set<Integer> key_set_add=listUserConnect.keySet();
                        String listUser="";
                        for(Integer key:key_set_add){
                        listUser+=listUserConnect.get(key)+":";
                        }
                        sendMessageToAll("3;"+listUser+";"+message[4]);
                        break;
                    //logout system
                    case 4:
                        listUserConnect.remove(Integer.parseInt(message[1]));
                       
                         Set<Integer> key_set_remove=listUserConnect.keySet();
                        String listUserUpdate="";
                        for(Integer key:key_set_remove){
                        listUserUpdate+=listUserConnect.get(key)+":";
                        }
                        sendMessageToAll("4;"+listUserUpdate+";"+message[4]);
                        break;
                }
            }
    }
    public DatagramPacket recieve(){
        try {
            byte[] recieveData = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(recieveData, recieveData.length);
            server.receive(datagramPacket);
            return datagramPacket;
        } catch (IOException ex) {
            Logger.getLogger(ServerStart.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void sendMess(String message,int port) {
        try {
            byte[] dataSend = new byte[1024];
            dataSend = message.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(dataSend, dataSend.length,InetAddress.getByName("localhost"),port);
            server.send(datagramPacket);
        } catch (IOException ex) {
            Logger.getLogger(ServerStart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public  void sendMessageToAll(String mess){
                Set<Integer> keySet=listUserConnect.keySet();
                for(Integer key:keySet){
                    sendMess(mess,key);
                }
    }
    @Override
    public void run() {
        ServerStart t = new ServerStart();
        t.openServer();
        t.StartServer();
    }
    public static void main(String[] args) {
        ServerStart ss= new ServerStart();
        new Thread(ss).start();
    }
}
