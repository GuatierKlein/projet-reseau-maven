package fr.miage.reseau.Worker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class Worker implements Runnable {
    private final ProtocolInterpreter interpeter;
    private final String serverAddress;
    private final int port;

    public Worker(String serverAddress, String password, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
        interpeter = new ProtocolInterpreter(password);
    }

    @Override
    public void run() {
        System.out.println("Connexion en cours...");
        try {                        
            Socket clientSocket = new Socket(serverAddress, port);
            final DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            final BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            interpeter.setOutToServer(outToServer);

            try {
                String response;
                while ((response = inFromServer.readLine()) != null) {
                    // System.out.println("Recu du serveur : " + response);
                    try {
                        interpeter.execute(new Message(response));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }   
            }
            } catch (SocketException e) {
                System.out.println("Connexion perdue");
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            clientSocket.close();
            System.out.println("Connexion fermée");
        } catch (SocketException e) {
            System.out.println("Connexion échouée");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
