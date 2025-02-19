package NTP;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Enumeration;
import java.util.Scanner;

public class Client {

    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;
    InetAddress localHost = getLocalIpAddress();

    public Client(String serverAddress, int serverPort, int clientPort) throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket(clientPort);
        this.serverAddress = InetAddress.getByName(serverAddress);
        this.serverPort = serverPort;
    }

    public void sendMessage(String message, InetAddress address, int port) {
        try {
            long heureActuelle = System.currentTimeMillis();
            byte[] msgBytes = message.getBytes();
            byte[] timeBytes = Long.toString(heureActuelle).getBytes(StandardCharsets.UTF_8);

            DatagramPacket packet = new DatagramPacket(timeBytes, timeBytes.length, address, port);
            socket.send(packet);
            // Envoyer une copie du message au serveur
            DatagramPacket serverPacket = new DatagramPacket(timeBytes, timeBytes.length, serverAddress, serverPort);
            socket.send(serverPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Message reçu de  " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " - " + receivedMessage );

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <NetworkInterfaces> InetAddress getLocalIpAddress() throws SocketException {
        Enumeration<NetworkInterfaces> interfaces = (Enumeration<NetworkInterfaces>) NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = (NetworkInterface) interfaces.nextElement();
            if (iface.isLoopback() || !iface.isUp()) continue;

            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address) {
                    return addr;
                }
            }
        }
        throw new SocketException("Aucune adresse IP locale disponible");
    }



    public void start() {
        try {
            System.out.println("Adresse IP du client 2 connexion : " + localHost.getHostAddress());
            System.out.println("Adresse IP du client : " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("Client démarré...");

            Thread receivingThread = new Thread(this::receiveMessage);
            receivingThread.start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Entrez votre message (ou 'quit' pour terminer) : ");


                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("quit")) {
                    break;
                }

                System.out.println("Entrez l'adresse IP du destinataire (ou 'all' pour envoyer à tous) : ");
                String destAddress = scanner.nextLine();
                if (destAddress.equalsIgnoreCase("all")) {
                    
                    sendMessage(message, serverAddress, serverPort);
                } else {
                    System.out.println("Entrez le port du destinataire : ");
                    int destPort = Integer.parseInt(scanner.nextLine());
                    sendMessage(message, InetAddress.getByName(destAddress), destPort);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }




    public static void main(String[] args) {
        try {
            String serverAddress = "192.168.56.1"; // Mettez l'adresse IP du serveur ici
            int serverPort = 6789; // Mettez le port du serveur ici
            int clientPort = 6790; // Mettez le port du client ici

            Client client = new Client(serverAddress, serverPort, clientPort);
            client.start();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
}