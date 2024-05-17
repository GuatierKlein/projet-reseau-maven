package fr.miage.reseau.Worker;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker {
    public void launch(String[] args) {
        try {
            final InetAddress bindAddress = InetAddress.getByName("0.0.0.0");
            ServerSocket socket = new ServerSocket(1337);

            while (true) {                
                final Socket clientSocket = socket.accept();
                final OutputStream out = clientSocket.getOutputStream();
                final InputStream in = clientSocket.getInputStream();

                StringBuffer sb = new StringBuffer();
                sb.append(in);
                String[] splitIn = sb.toString().split(" ");

                
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
