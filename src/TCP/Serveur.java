package TCP;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Serveur {
    private ServerSocket serverSocket;

    public Serveur(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() {
        try {
            System.out.println("Serveur démarré...");
            System.out.println("Adresse IP du serveur : " + InetAddress.getLocalHost().getHostAddress());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connecté: " + clientSocket.getInetAddress().getHostAddress());

                OutputStream outputStream = clientSocket.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream, true);

                LocalDateTime currentTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedCurrentTime = "L'heure actuelle est : " + currentTime.format(formatter);

                writer.println(formattedCurrentTime);

                writer.close();
                clientSocket.close();
                System.out.println("Connexion fermée avec le client.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            int port = 6789; // Port du serveur
            Serveur serveur = new Serveur(port);
            serveur.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
