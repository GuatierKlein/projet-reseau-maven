package fr.miage.reseau.Worker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

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
        // System.out.println("En Attente du serveur...");
        try {              
            Socket clientSocket = new Socket(serverAddress, port);
            final DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            final BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            interpeter.setOutToServer(outToServer);
            Message message = new Message(inFromServer.readLine());

            for (MessageLine line : message.getLines()) {
                try {
                    interpeter.execute(line);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
