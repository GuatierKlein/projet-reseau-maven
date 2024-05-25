package fr.miage.reseau.Worker;

import java.security.NoSuchAlgorithmException;

import fr.miage.reseau.Miner.Miner;

public class Task implements Runnable {
    private final Miner _miner;
    private final MessageSender _outToServer;

    public Task(String data, int difficulty, int startingNonce, int step, MessageSender outToServer) {
        if(difficulty == 0 || step == 0 || data == null)
            throw new IllegalArgumentException("Paramètres invalides");
        _miner = new Miner(data, difficulty, step, startingNonce);
        this._outToServer = outToServer;
    }

    public void terminate() {
        _miner.stop();
    }

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

    public String getCurrentNonce() {
        return _miner.getNonceHexString();
    }

}
