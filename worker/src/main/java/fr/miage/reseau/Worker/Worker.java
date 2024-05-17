package fr.miage.reseau.Worker;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Worker {
    private String password;
    private ProtocolInterpreter interpeter;

    public void launch(String[] args) {
        askForPwd();
        try {
            final InetAddress bindAddress = InetAddress.getByName("0.0.0.0");
            ServerSocket socket = new ServerSocket(1337);

            while (true) {                
                final Socket clientSocket = socket.accept();
                final OutputStream out = clientSocket.getOutputStream();
                final InputStream in = clientSocket.getInputStream();

                StringBuffer sb = new StringBuffer();
                sb.append(in);

                Message message = new Message(sb.toString());

                for (MessageLine line : message.getLines()) {
                    // ProtocolInterpreter interpeter = new ProtocolInterpreter(line);
                    //make interpreter static to store the worker thread
                    interpeter.execute(line);
                }
                
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
