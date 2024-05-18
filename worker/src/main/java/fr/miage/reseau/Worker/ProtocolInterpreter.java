package fr.miage.reseau.Worker;

import java.io.DataOutputStream;

import fr.miage.reseau.Miner.Miner;

public class ProtocolInterpreter {
    // private MessageLine message;
    private String password;
    private Thread workerThread;
    private int difficulty;
    private int step;
    private int startingNonce;
    private String data;
    private Miner miner;
    private MessageSender outToServer;

    public ProtocolInterpreter(String password) {
        this.password = password;
    }

    public void setOutToServer(DataOutputStream outToServer) {
        this.outToServer = new MessageSender(outToServer, password);
    }

    public void execute(Message message) throws Exception {
        switch (message.getCommand()) {
            case "WHO_ARE_YOU_?": WHO_ARE_YOU();
            break;
            case "GIMME_PASSWORD": GIMME_PASSWORD();
            break; 
            case "HELLO_YOU": HELLO_YOU();
            break;
            case "OK": OK();
            break;
            case "NONCE": NONCE(message);
            break;
            case "PAYLOAD": PAYLOAD(message);
            break; 
            case "SOLVE": SOLVE(message);
            break;
            case "YOU_DONT_FOOL_ME": YOU_DONT_FOOL_ME();
            break;
            case "PROGRESS": PROGRESS();
            break;
            case "CANCELLED": CANCELLED();
            break;
            case "SOLVED": SOLVED();
            break;
        
            default: throw new Exception("Commande inconnue : " + message.getCommand());
        }
    }

    private void SOLVE(Message message) throws Exception {
        System.out.println("Minage demandé par le serveur");
        //vérifier le parse
        difficulty = Integer.parseInt(message.getArg1());
        if(data == null || step == 0)
            throw new Exception("Paramètres manquants");
        miner = new Miner(data, difficulty, step, startingNonce);
        workerThread = new Thread(miner);
        workerThread.start(); //comment on s'arrete quand on a trouvé?
        System.out.println("Minage debuté...");
        workerThread.join();
        System.out.println("FOUND! Solution trouvée");
        outToServer.FOUND(miner.getHash(), miner.getNonceHexString());
        System.out.println("Réponse envoyée");
        outToServer.READY();
        System.out.println("En attente du serveur...");
    }

    private void WHO_ARE_YOU() {
        System.out.println("Connecté");
        outToServer.ITS_ME();
    }

    private void GIMME_PASSWORD() {
        System.out.println("Mot de passe demandé");
        outToServer.PASSWD();
    }

    private void HELLO_YOU() {
        System.out.println("Mot de passe validé par le serveur");
        outToServer.READY();
    }

    private void OK() {
        //nada
    }

    private void NONCE(Message message) {
        try {
            startingNonce = Integer.parseInt(message.getArg1());
            step = Integer.parseInt(message.getArg2());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Paramètres nonce reçus");
    }

    private void PAYLOAD(Message message) {
        data = message.getArg1();
    }

    private void YOU_DONT_FOOL_ME() {
        System.out.println("Connection refusée");
        System.exit(0);
    }

    private void PROGRESS() {
        System.out.println("Progrès demandé");
        if(data == null || step == 0)
        {
            outToServer.STATUS_IDLE();
            return;
        }

        if(miner.isWorking()) {
            outToServer.sendProgress(miner.getNonceHexString());
            return;
        }

        outToServer.STATUS_READY();
    }

    private void CANCELLED() throws InterruptedException {
        workerThread.interrupt();
        workerThread.join();
        outToServer.READY();
        System.out.println("Minage arreté par le serveur");
    }

    private void SOLVED() throws InterruptedException {
        System.out.println("Solution trouvée par une autre machine");
        CANCELLED();
    }
}
