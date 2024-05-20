package fr.miage.reseau.Worker;

import java.security.NoSuchAlgorithmException;

import fr.miage.reseau.Miner.Miner;

public class Task implements Runnable {
    private final Miner miner;
    private final MessageSender outToServer;

    public Task(String data, int difficulty, int startingNonce, int step, MessageSender outToServer) {
        if(difficulty == 0 || step == 0 || data == null)
            throw new IllegalArgumentException("Paramètres invalides");
        miner = new Miner(data, difficulty, step, startingNonce);
        this.outToServer = outToServer;
    }

    public void terminate() {
        miner.stop();
    }

    @Override
    public void run() {
        System.out.println("Minage débuté...");
        miner.computeNonce();
        if(!miner.didFind())
            return;
        System.out.println("FOUND! Solution trouvée");
        try {
            outToServer.FOUND(miner.getHash(), miner.getNonceHexString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("Réponse envoyée");
    }

    public String getCurrentNonce() {
        return miner.getNonceHexString();
    }

}
