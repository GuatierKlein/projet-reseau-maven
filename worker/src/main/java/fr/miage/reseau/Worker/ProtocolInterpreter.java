package fr.miage.reseau.Worker;

import java.io.DataOutputStream;
import java.security.NoSuchAlgorithmException;

public class ProtocolInterpreter {
    private String _password;
    private Thread _workerThread;
    private int _difficulty;
    private int _step;
    private int _startingNonce;
    private String _data;
    private Task _tasker;
    private MessageSender _outToServer;

    public ProtocolInterpreter(String password) {
        this._password = password;
    }

    public void setOutToServer(DataOutputStream outToServer) {
        this._outToServer = new MessageSender(outToServer, _password);
    }

    public void execute(Message message) throws Exception {
        switch (message.get_command()) {
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
        
            default: throw new Exception("Commande inconnue : " + message.get_command());
        }
    }

    private void SOLVE(Message message) throws InterruptedException, NoSuchAlgorithmException {
        System.out.println("Difficulté reçue");
        _difficulty = Integer.parseInt(message.get_arg1());
        tryStartSolving();
    }

    private void WHO_ARE_YOU() {
        System.out.println("Authentification demandée");
        _outToServer.ITS_ME();
    }

    private void GIMME_PASSWORD() {
        System.out.println("Mot de passe demandé");
        _outToServer.PASSWD();
    }

    private void HELLO_YOU() {
        System.out.println("Mot de passe validé par le serveur");
        System.out.println();
        _outToServer.READY();
    }

    private void OK() {
        //nada
    }

    private void NONCE(Message message) throws InterruptedException, NoSuchAlgorithmException {
        try {
            _startingNonce = Integer.parseInt(message.get_arg1());
            _step = Integer.parseInt(message.get_arg2());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Paramètres nonce reçus");
        tryStartSolving();
    }

    private void PAYLOAD(Message message) throws InterruptedException, NoSuchAlgorithmException {
        _data = message.get_arg1();
        System.out.println("Payload reçue");
        tryStartSolving();
    }

    private void YOU_DONT_FOOL_ME() {
        System.out.println("Connection refusée (mauvais mot de passe)");
        System.exit(0);
    }

    private void PROGRESS() {
        System.out.println("Progrès demandé");
        if(_data == null || _step == 0)
        {
            _outToServer.STATUS_IDLE();
            return;
        }

        if(_workerThread.isAlive()) {
            _outToServer.sendProgress(_tasker.getCurrentNonce());
            return;
        }

        _outToServer.STATUS_READY();
    }

    private void CANCELLED() throws InterruptedException {
        _tasker.terminate();
        System.out.println("Minage arreté par le serveur");
    }

    private void SOLVED() throws InterruptedException {
        System.out.println("Solution trouvée par une autre machine");
        CANCELLED();
    }

    private void tryStartSolving() throws InterruptedException, NoSuchAlgorithmException {
        if(_data == null || _step == 0 || _difficulty == 0)
            return;
        if(_workerThread != null && _workerThread.isAlive())
            return;
        _tasker = new Task(_data, _difficulty, _startingNonce, _step, _outToServer);
        _workerThread = new Thread(_tasker);
        _workerThread.start(); 
    }
}
