package UDP;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Serveur {

    private int cpt;

    public static void main(String[] args) {
        try{
            int port = 12345;
            DatagramSocket listener = new DatagramSocket(port);
            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

            while(true){
                byte[] buf1 = new byte[1024];
                DatagramPacket packet1 = new DatagramPacket(buf1, buf1.length);
                byte[] buf2 = new byte[1024];
                DatagramPacket packet2 = new DatagramPacket(buf2, buf2.length);

                listener.receive(packet1);
                listener.receive(packet2);
                pool.execute(new Communication(packet1, packet2));

            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
