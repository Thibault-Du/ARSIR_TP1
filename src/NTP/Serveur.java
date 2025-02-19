package NTP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Serveur {



    private DatagramSocket socket;
    private List<String> messageHistory;

    public Serveur(int port) throws SocketException {
        socket = new DatagramSocket(port);
        messageHistory = new ArrayList<>();
    }

    public void start() {
        try {
            // Obtenir et afficher l'adresse IP du serveur
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("Serveur démarré...");
            System.out.println("Adresse IP du serveur : " + localHost.getHostAddress());

            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();


                LocalDateTime currentTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedCurrentTime = currentTime.format(formatter);


                String heuremessage = "L'heure actuelle est : " + formattedCurrentTime;
                byte[] confirmationBytes = heuremessage.getBytes();
                DatagramPacket confirmationPacket = new DatagramPacket(confirmationBytes, confirmationBytes.length, clientAddress, clientPort);
                socket.send(confirmationPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }




    public static void main(String[] args) {
        try {
            int port = 6789; // Mettez le port du serveur ici
            Serveur serveur = new Serveur(port);
            serveur.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
