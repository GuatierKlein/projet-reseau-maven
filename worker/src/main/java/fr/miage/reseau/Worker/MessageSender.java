package fr.miage.reseau.Worker;

import java.io.DataOutputStream;
import java.io.IOException;

public class MessageSender {
    private DataOutputStream outToServer;
    private String password;

    public MessageSender(DataOutputStream outToServer, String password) {
        this.outToServer = outToServer;
    }

    private void send(String message) {
        try {
            outToServer.writeBytes(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ITS_ME() {
        send("ITS_ME\n");
    }

    public void PASSWD() {
        StringBuffer sb = new StringBuffer();
        sb.append("PASSWD ");
        sb.append(password);
        sb.append("\n");
        send(sb.toString());
    }

    public void sendProgress(String nonce) {
        StringBuffer sb = new StringBuffer();
        sb.append(nonce);
        sb.append("\n");
        send(sb.toString());
    }

    public void READY() {
        send("READY\n");
    }

    public void IDLE() {
        send("IDLE\n");
    }

    public void FOUND(String hash, String nonce) {
        StringBuffer sb = new StringBuffer();
        sb.append("FOUND ");
        sb.append(hash);
        sb.append(" ");
        sb.append(nonce);
        sb.append("\n");
        send(sb.toString());
    }
}
