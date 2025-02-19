package UDP;

import java.io.IOException;
import java.net.*;
import java.time.LocalTime;
import java.util.Enumeration;
import java.util.Scanner;

public class Client {

    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private InetAddress[] clientAddresses;
    private int[] clientPorts;
    InetAddress localHost = getLocalIpAddress();

    public Client(String serverAddress, int serverPort, int clientPort, InetAddress[] clientAddresses, int[] clientPorts) throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket(clientPort);
        this.serverAddress = InetAddress.getByName(serverAddress);
        this.serverPort = serverPort;
        this.clientAddresses = clientAddresses;
        this.clientPorts = clientPorts;
    }

    public void sendMessage(String message, InetAddress address, int port) {
        try {
            LocalTime heureActuelle = LocalTime.now();


            System.out.println("L'heure actuelle est : " + heureActuelle);


            LocalTime heureStockee = heureActuelle;

            System.out.println("L'heure stockée est : " + heureStockee);
            byte[] msgBytes = message.getBytes();
            DatagramPacket packet = new DatagramPacket(msgBytes, msgBytes.length, address, port);
            socket.send(packet);
            // Envoyer une copie du message au serveur
            DatagramPacket serverPacket = new DatagramPacket(msgBytes, msgBytes.length, serverAddress, serverPort);
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
                System.out.println("Message reçu de  " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " - " + receivedMessage);
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
            String serverAddress = "10.42.135.71"; // Mettez l'adresse IP du serveur ici
            int serverPort = 6789; // Mettez le port du serveur ici
            int clientPort = 6790; // Mettez le port du client ici
            InetAddress[] clientAddresses = {
                    InetAddress.getByName("192.168.0.101"),
                    InetAddress.getByName("192.168.0.103")
            }; // Adresses IP des autres clients
            int[] clientPorts = {6791, 6792}; // Ports des autres clients

            Client client = new Client(serverAddress, serverPort, clientPort, clientAddresses, clientPorts);
            client.start();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public static void main1(String[] args) {
        try {
            String serverAddress = "10.42.135.71"; // Mettez l'adresse IP du serveur ici
            int serverPort = 6789; // Mettez le port du serveur ici
            int clientPort = 6790; // Mettez le port du client ici
            InetAddress[] clientAddresses = {
                    InetAddress.getByName("192.168.0.101"),
                    InetAddress.getByName("192.168.0.103")
            }; // Adresses IP des autres clients
            int[] clientPorts = {6791, 6792}; // Ports des autres clients

            Client client = new Client(serverAddress, serverPort, clientPort, clientAddresses, clientPorts);
            client.start();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
}