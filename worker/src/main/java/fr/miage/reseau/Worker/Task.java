package fr.miage.reseau.Worker;

import java.security.NoSuchAlgorithmException;
import fr.miage.reseau.Miner.Miner;

/**
 * La classe Task représente une tâche de minage qui peut être exécutée dans un thread séparé.
 */
public class Task implements Runnable {
    private final Miner _miner;
    private final MessageSender _outToServer;

    /**
     * Constructeur de la classe Task.
     *
     * @param data les données à miner
     * @param difficulty la difficulté du minage
     * @param startingNonce le nonce de départ
     * @param step le pas d'incrémentation du nonce
     * @param outToServer l'objet pour envoyer des messages au serveur
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public Task(String data, int difficulty, int startingNonce, int step, MessageSender outToServer) {
        if(difficulty == 0 || step == 0 || data == null)
            throw new IllegalArgumentException("Paramètres invalides");
        _miner = new Miner(data, difficulty, step, startingNonce);
        this._outToServer = outToServer;
    }

    /**
     * Termine la tâche de minage en cours.
     */
    public void terminate() {
        _miner.stop();
    }

    /**
     * Méthode exécutée lorsque la tâche est démarrée dans un thread. Effectue le minage
     * et envoie le résultat au serveur si une solution est trouvée.
     */
    @Override
    public void run() {
        System.out.println("Minage débuté...");
        _miner.computeNonce();
        if(!_miner.didFind())
            return;
        System.out.println("FOUND! Solution trouvée");
        try {
            _outToServer.FOUND(_miner.getHash(), _miner.getNonceHexString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("Réponse envoyée");
    }

    /**
     * Retourne le nonce actuel sous forme de chaîne hexadécimale.
     *
     * @return le nonce actuel sous forme de chaîne hexadécimale
     */
    public String getCurrentNonce() {
        return _miner.getNonceHexString();
    }
}
