package fr.miage.reseau.Worker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Worker {
    private String password;
    private ProtocolInterpreter interpeter;

    public void launch(String serverAddress, int port) {
        askForPwd();
        interpeter = new ProtocolInterpreter(password);
        try {
            while (true) {                
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void askForPwd() {
        System.out.print("Mot de passe à envoyer au serveur : ");
        Scanner input = new Scanner(System.in);
        password = input.nextLine();
        input.close();
    }
}
