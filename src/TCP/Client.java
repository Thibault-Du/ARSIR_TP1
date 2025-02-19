package TCP;

import java.io.*;
import java.net.*;

public class Client {
    private String serverAddress;
    private int serverPort;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void requestTime() {
        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String serverResponse = reader.readLine();
            System.out.println("Réponse du serveur : " + serverResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // Adresse du serveur
        int serverPort = 6789; // Port du serveur

        Client client = new Client(serverAddress, serverPort);
        client.requestTime();
    }
}