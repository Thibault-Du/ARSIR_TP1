package UDP;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {

    private static DatagramSocket ds;
    private static Scanner scan;
    private static InetAddress ip_server;
    private static InetAddress ip_salon;
    private static int port_salon;
    private static Thread receive;
    private static Thread send;

    public static void main(String[] args) {
        try{
            //InetAddress ip = InetAddress.getByName("172.20.10.2");
            ip_server = InetAddress.getLocalHost();
            ds = new DatagramSocket();
            scan = new Scanner(System.in);
            connection();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void connection() throws IOException {
        byte[] buf = new byte[1024];

        if(send!=null){
            send.stop();
            send = null;
        }
        if(receive!=null){
            receive.interrupt();
            receive = null;
        }

        System.out.println("Entrez un message :");

        scan = new Scanner(System.in);
        String m = scan.nextLine();

        byte[] mess = m.getBytes();
        for(int i = 0; i < mess.length; i++){
            buf[i] = mess[i];
        }

        System.out.println("hello serveur");
        DatagramPacket dp = new DatagramPacket(buf, buf.length, ip_server, 12345);
        ds.send(dp);

        byte[] buf2 = new byte[1024];
        DatagramPacket dp2 = new DatagramPacket(buf2, buf2.length);
        ds.receive(dp2);
        String message = new String(dp2.getData()).trim();
        ip_salon = dp2.getAddress();
        port_salon = dp2.getPort();
        System.out.println("Serveur ready : " + dp2.getAddress().getHostAddress() + ":" + dp2.getPort()) ;
        System.out.println(message);

        receiver();
        sender();

    }

    public static void receiver(){
        receive=new Thread(){
            public void run() {
                try{
                    while(true){
                        byte[] buf2 = new byte[1024];
                        DatagramPacket dp2 = new DatagramPacket(buf2, buf2.length);
                        ds.receive(dp2);
                        String message = new String(dp2.getData()).trim();
                        if((dp2.getData())[0] == "/".getBytes()[0]){
                            switch(new String(dp2.getData()).trim()){
                                case "/quit" :
                                    System.exit(0);
                                    break;
                                case "/skip" :
                                    connection();
                                    break;
                            }
                        } else {
                            System.out.println(message);
                        }
                    }
                } catch(IOException v){System.out.println(v);}
            }
        };
        receive.start();
    }

    public static void sender(){
        send=new Thread(){
            public void run() {
                try{
                    while(true){
                        byte[] buf = new byte[1024];

                        String userName = scan.nextLine();

                        byte[] mess = userName.getBytes();
                        for(int i = 0; i < mess.length; i++){
                            buf[i] = mess[i];
                        }
                        DatagramPacket dp = new DatagramPacket(buf, buf.length, ip_salon, port_salon);
                        ds.send(dp);
                    }
                } catch(IOException v){System.out.println(v);}
            }
        };
        send.start();
    }
}
