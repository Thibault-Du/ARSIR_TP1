package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Communication implements Runnable{

    private DatagramSocket socket;
    private DatagramPacket packet1;
    private DatagramPacket packet2;


    public Communication(DatagramPacket packet1, DatagramPacket p2){
        this.packet1 = packet1;
        this.packet2 = p2;
        try{
            socket = new DatagramSocket();
        } catch(SocketException e){
            e.printStackTrace();
        }

    }

    public void run() {
        System.out.println("Nouveau client : " + packet1.getAddress().getHostAddress() + ":" + packet1.getPort());
        try {
            byte[] message1 = packet1.getData();
            System.out.println("Message : " + new String(message1).trim());

            DatagramPacket dp = new DatagramPacket(message1, message1.length, packet2.getAddress() , packet2.getPort());
            socket.send(dp);

            byte[] message2 = packet2.getData();
            System.out.println("Message : " + new String(message1).trim());

            DatagramPacket dp2 = new DatagramPacket(message2, message2.length, packet1.getAddress() , packet1.getPort());
            socket.send(dp2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread receive=new Thread(){
            public void run() {
                try{
                    while(true){
                        byte[] buf1 = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(buf1, buf1.length);
                        socket.receive(packet);
                        byte[] message1 = packet.getData();

                        DatagramPacket dp1;
                        DatagramPacket dp2;
                        if(message1[0] == "/".getBytes()[0]){
                            switch(new String(message1).trim()){
                                case "/quit" :
                                    if (packet.getAddress().equals(packet1.getAddress()) && packet.getPort() == packet1.getPort()) {
                                        dp1 = new DatagramPacket(message1, message1.length, packet1.getAddress(), packet1.getPort());
                                        var skip = "/skip".getBytes();
                                        dp2 = new DatagramPacket(skip, skip.length, packet2.getAddress(), packet2.getPort());
                                        socket.send(dp1);
                                        socket.send(dp2);
                                    } else {
                                        dp1 = new DatagramPacket(message1, message1.length, packet2.getAddress(), packet2.getPort());
                                        var skip = "/skip".getBytes();
                                        dp2 = new DatagramPacket(skip, skip.length, packet1.getAddress(), packet1.getPort());
                                        socket.send(dp1);
                                        socket.send(dp2);
                                    }

                                    socket.close();
                                    break;
                                case "/skip" :
                                    dp1 = new DatagramPacket(message1, message1.length, packet1.getAddress(), packet1.getPort());
                                    dp2 = new DatagramPacket(message1, message1.length, packet2.getAddress(), packet2.getPort());
                                    socket.send(dp1);
                                    socket.send(dp2);
                                    socket.close();
                                    break;
                            }
                        } else {
                            if (packet.getAddress().equals(packet1.getAddress()) && packet.getPort() == packet1.getPort()) {
                                dp2 = new DatagramPacket(message1, message1.length, packet2.getAddress(), packet2.getPort());
                                socket.send(dp2);
                            } else {
                                dp1 = new DatagramPacket(message1, message1.length, packet1.getAddress(), packet1.getPort());
                                socket.send(dp1);
                            }
                        }
                    }
                } catch(IOException v){System.out.println(v);}
            }
        };
        receive.start();

    }
}