package UDP;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> ports = new ArrayList<Integer>();
        for(int i = 0; i < 100; i++) {
            ports.add(i);
        }
        scannerUDP(ports);
    }

    public static void scannerUDP(ArrayList<Integer> ports){
        try {
            DatagramSocket socket = new DatagramSocket(33);
        }
        catch (SocketException e) {

        }
        for (Integer port : ports) {
            try {
                DatagramSocket socket = new DatagramSocket(port);
                System.out.println("port " + port + " : ouvert");
                socket.close();
            }
            catch (SocketException e) {
                System.out.println("port " + port + " : fermé");
            }
        }
    }
}