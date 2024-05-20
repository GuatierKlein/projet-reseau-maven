package fr.miage.reseau.Worker;

import java.io.DataOutputStream;
import java.security.NoSuchAlgorithmException;

public class ProtocolInterpreter {
    // private MessageLine message;
    private String password;
    private Thread workerThread;
    private int difficulty;
    private int step;
    private int startingNonce;
    private String data;
    private Task tasker;
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

    private void SOLVE(Message message) throws InterruptedException, NoSuchAlgorithmException {
        System.out.println("Difficulté reçue");
        difficulty = Integer.parseInt(message.getArg1());
        tryStartSolving();
    }

    private void WHO_ARE_YOU() {
        System.out.println("Authentification demandée");
        outToServer.ITS_ME();
    }

    private void GIMME_PASSWORD() {
        System.out.println("Mot de passe demandé");
        outToServer.PASSWD();
    }

    private void HELLO_YOU() {
        System.out.println("Mot de passe validé par le serveur");
        System.out.println();
        outToServer.READY();
    }

    private void OK() {
        //nada
    }

    private void NONCE(Message message) throws InterruptedException, NoSuchAlgorithmException {
        try {
            startingNonce = Integer.parseInt(message.getArg1());
            step = Integer.parseInt(message.getArg2());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Paramètres nonce reçus");
        tryStartSolving();
    }

    private void PAYLOAD(Message message) throws InterruptedException, NoSuchAlgorithmException {
        data = message.getArg1();
        System.out.println("Payload reçue");
        tryStartSolving();
    }

    private void YOU_DONT_FOOL_ME() {
        System.out.println("Connection refusée (mauvais mot de passe)");
        System.exit(0);
    }

    private void PROGRESS() {
        System.out.println("Progrès demandé");
        if(data == null || step == 0)
        {
            outToServer.STATUS_IDLE();
            return;
        }

        if(workerThread.isAlive()) {
            outToServer.sendProgress(tasker.getCurrentNonce());
            return;
        }

        outToServer.STATUS_READY();
    }

    private void CANCELLED() throws InterruptedException {
        tasker.terminate();
        System.out.println("Minage arreté par le serveur");
    }

    private void SOLVED() throws InterruptedException {
        System.out.println("Solution trouvée par une autre machine");
        CANCELLED();
    }

    private void tryStartSolving() throws InterruptedException, NoSuchAlgorithmException {
        if(data == null || step == 0 || difficulty == 0)
            return;
        if(workerThread != null && workerThread.isAlive())
            return;
        tasker = new Task(data, difficulty, startingNonce, step, outToServer);
        workerThread = new Thread(tasker);
        workerThread.start(); 
    }
}
