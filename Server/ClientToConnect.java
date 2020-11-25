/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import chatclient.Client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HoangHung
 */
public class ClientToConnect implements Runnable {

    private DatagramSocket client;
    private Hashtable<String, DatagramSocket> listUserConnect = new Hashtable<>();

    public ClientToConnect(DatagramSocket client, Hashtable<String, DatagramSocket> listUser) {
        this.client = client;
        this.listUserConnect = listUser;
    }

    // tạo luồng luôn đọc dữ liệu từ client gửi đến server
//    byte[] receiveData = new byte[1024];
//        byte[] sendData = new byte[1024];
//        while (true) {
//            try {
//                // Nhan du lieu
//                DatagramPacket receivePacket = new DatagramPacket(receiveData,
//                        receiveData.length);
//                myServer.receive(receivePacket);
//                input = new String(receivePacket.getData());
//                // Xu li du lieu
//                ReverseString obj = new ReverseString(input);
//                obj.reverse();
//                InetAddress IPAddress = receivePacket.getAddress();
//                int port = receivePacket.getPort();
//                list.add(receivePacket);
//                for(DatagramPacket i:list){
//                    System.out.println(i.getAddress()+" "+ i.getPort());
//                }
//                sendData = obj.getStr().getBytes();
//                DatagramPacket sendPacket = new DatagramPacket(sendData,
//                        sendData.length, IPAddress, port);
    @Override
    public void run() {
//        try {
            byte[] recieveData = new byte[1024];
            while (true) {
                int event = Integer.parseInt(recieve());
                System.out.println(event);

//                switch (event) {
//                    // đọc dữ liệu message mà client gửi tới và ghi tới tất cả các client
//                    case 1:
//                    try {
//                        
//                        String messRecieve = in.readUTF();
//                        Set<String> keySet = listUserConnect.keySet();
//                        for (String key : keySet) {
//                            out = new DataOutputStream(listUserConnect.get(key).getOutputStream());
//                            System.out.println(key + " " + listUserConnect.get(key));
//                            out.writeInt(event);
//                            out.writeUTF(messRecieve);
//                        }
//                        
//                    } catch (IOException ex) {
//                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    break;
//                    // update danh sách các user online
//                    case 3:
//                        String userlogout=in.readUTF();
//                        Set<String> keySet = listUserConnect.keySet();
//                        for (String key : keySet) {
//                            out = new DataOutputStream(listUserConnect.get(key).getOutputStream());
//                            out.writeInt(event);
//                            out.writeUTF(userlogout);
//                        }
//                        break;
//                    case 4:
//                }
//            }
//
//        } catch (IOException ex) {
//            //Nếu một ngoại lệ xảy ra, điều an toàn nhất là do máy khách đã ngắt kết nối nên chúng tôi xóa nó khỏi danh sách kết nối
//            Set<String> keySet = listUserConnect.keySet();
//            for (String key:keySet) {
//                if (listUserConnect.get(key) == client) {
//                    listUserConnect.remove(key);
//                    break;
//                }
//            }
//            }
        }
    }

    public String recieve() {
        if (client != null) {
            byte[] recieveData = new byte[1024];
            try {
                DatagramPacket datagramPacket = new DatagramPacket(recieveData, recieveData.length);
                client.receive(datagramPacket);
                return new String(datagramPacket.getData());
            } catch (IOException ex) {
                Logger.getLogger(ClientToConnect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public void send(DatagramSocket s) {
        if (client != null) {
            byte[] dataSent = new byte[1024];
            try {
                DatagramPacket datagramPacket = new DatagramPacket(dataSent, dataSent.length, s.getInetAddress(), s.getPort());
                client.send(datagramPacket);
            } catch (IOException ex) {
                Logger.getLogger(ClientToConnect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
