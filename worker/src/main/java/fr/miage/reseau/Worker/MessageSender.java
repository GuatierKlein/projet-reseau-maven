package fr.miage.reseau.Worker;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * La classe MessageSender est responsable de l'envoi de messages au serveur via un flux de sortie de données.
 */
public class MessageSender {
    private DataOutputStream outToServer;
    private String password;

    /**
     * Constructeur de la classe MessageSender.
     *
     * @param outToServer le flux de sortie de données vers le serveur
     * @param password le mot de passe utilisé pour l'authentification
     */
    public MessageSender(DataOutputStream outToServer, String password) {
        this.outToServer = outToServer;
        this.password = password;
    }

    /**
     * Envoie un message au serveur.
     *
     * @param message le message à envoyer
     */
    private void send(String message) {
        // System.out.printf("Sending %s", message);
        try {
            outToServer.writeBytes(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envoie le message d'authentification "ITS_ME" au serveur.
     */
    public void ITS_ME() {
        send("ITS_ME\n");
    }

    /**
     * Envoie le mot de passe au serveur avec le message "PASSWD".
     */
    public void PASSWD() {
        StringBuffer sb = new StringBuffer();
        sb.append("PASSWD ");
        sb.append(password);
        sb.append("\n");
        send(sb.toString());
    }

    /**
     * Envoie le progrès actuel de calcul du nonce au serveur avec le message "STATUS".
     *
     * @param nonce le nonce courant à envoyer
     */
    public void sendProgress(String nonce) {
        StringBuffer sb = new StringBuffer();
        sb.append("STATUS ");
        sb.append(nonce);
        sb.append("\n");
        send(sb.toString());
    }

    /**
     * Envoie le message "READY" au serveur pour indiquer que le client est prêt.
     */
    public void READY() {
        send("READY\n");
    }

    /**
     * Envoie le message "STATUS IDLE" au serveur pour indiquer que le client est en mode inactif.
     */
    public void STATUS_IDLE() {
        send("STATUS IDLE\n");
    }

    /**
     * Envoie le message "STATUS READY" au serveur pour indiquer que le client est prêt à recevoir des tâches.
     */
    public void STATUS_READY() {
        send("STATUS READY\n");
    }

    /**
     * Envoie le message "FOUND" au serveur pour indiquer qu'une solution a été trouvée.
     *
     * @param hash le hachage de la solution trouvée
     * @param nonce le nonce correspondant à la solution trouvée
     */
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
