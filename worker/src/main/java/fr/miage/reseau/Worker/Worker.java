package fr.miage.reseau.Worker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
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

            //reader Thread
              Thread readerThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = inFromServer.readLine()) != null) {
                        System.out.println("Recu du serveur : " + response);
                        Message message = new Message(response); 
                        for (MessageLine line : message.getLines()) {
                            try {
                                interpeter.execute(line);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            readerThread.start();
            // clientSocket.close();
        } catch (Exception e) {
            System.out.println("Connection echoue");
            e.printStackTrace();
        }
    }
}
