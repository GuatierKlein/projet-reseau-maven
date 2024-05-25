package fr.miage.reseau.Worker;

import java.io.DataOutputStream;
import java.security.NoSuchAlgorithmException;

/**
 * La classe ProtocolInterpreter est responsable de l'interprétation des messages
 * reçus et de l'exécution des actions appropriées en fonction de la commande du message.
 */
public class ProtocolInterpreter {
    private String _password;
    private Thread _workerThread;
    private int _difficulty;
    private int _step;
    private int _startingNonce;
    private String _data;
    private Task _tasker;
    private MessageSender _outToServer;

    /**
     * Constructeur de la classe ProtocolInterpreter.
     *
     * @param password le mot de passe utilisé pour l'authentification
     */
    public ProtocolInterpreter(String password) {
        this._password = password;
    }

    /**
     * Définit le flux de sortie de données pour l'envoi de messages au serveur.
     *
     * @param outToServer le flux de sortie de données
     */
    public void setOutToServer(DataOutputStream outToServer) {
        this._outToServer = new MessageSender(outToServer, _password);
    }

    /**
     * Exécute l'action appropriée en fonction de la commande du message reçu.
     *
     * @param message le message reçu
     * @throws Exception si une commande inconnue est reçue
     */
    public void execute(Message message) throws Exception {
        switch (message.get_command()) {
            case "WHO_ARE_YOU_?": WHO_ARE_YOU(); break;
            case "GIMME_PASSWORD": GIMME_PASSWORD(); break;
            case "HELLO_YOU": HELLO_YOU(); break;
            case "OK": OK(); break;
            case "NONCE": NONCE(message); break;
            case "PAYLOAD": PAYLOAD(message); break;
            case "SOLVE": SOLVE(message); break;
            case "YOU_DONT_FOOL_ME": YOU_DONT_FOOL_ME(); break;
            case "PROGRESS": PROGRESS(); break;
            case "CANCELLED": CANCELLED(); break;
            case "SOLVED": SOLVED(); break;
            default: throw new Exception("Commande inconnue : " + message.get_command());
        }
    }

    /**
     * Traite la commande "SOLVE" en définissant la difficulté et en essayant de démarrer le calcul.
     *
     * @param message le message contenant la commande et les arguments
     * @throws InterruptedException si l'opération est interrompue
     * @throws NoSuchAlgorithmException si l'algorithme de hachage n'est pas trouvé
     */
    private void SOLVE(Message message) throws InterruptedException, NoSuchAlgorithmException {
        System.out.println("Difficulté reçue");
        _difficulty = Integer.parseInt(message.get_arg1());
        tryStartSolving();
    }

    /**
     * Traite la commande "WHO_ARE_YOU_?" en envoyant le message "ITS_ME" au serveur.
     */
    private void WHO_ARE_YOU() {
        System.out.println("Authentification demandée");
        _outToServer.ITS_ME();
    }

    /**
     * Traite la commande "GIMME_PASSWORD" en envoyant le mot de passe au serveur.
     */
    private void GIMME_PASSWORD() {
        System.out.println("Mot de passe demandé");
        _outToServer.PASSWD();
    }

    /**
     * Traite la commande "HELLO_YOU" en indiquant que le mot de passe est validé
     * et en envoyant le message "READY" au serveur.
     */
    private void HELLO_YOU() {
        System.out.println("Mot de passe validé par le serveur");
        System.out.println();
        _outToServer.READY();
    }

    /**
     * Traite la commande "OK". Actuellement, cette commande ne fait rien.
     */
    private void OK() {
        // nada
    }

    /**
     * Traite la commande "NONCE" en définissant les paramètres de nonce et en essayant de démarrer le calcul.
     *
     * @param message le message contenant la commande et les arguments
     * @throws InterruptedException si l'opération est interrompue
     * @throws NoSuchAlgorithmException si l'algorithme de hachage n'est pas trouvé
     */
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

    /**
     * Traite la commande "PAYLOAD" en définissant les données et en essayant de démarrer le calcul.
     *
     * @param message le message contenant la commande et les arguments
     * @throws InterruptedException si l'opération est interrompue
     * @throws NoSuchAlgorithmException si l'algorithme de hachage n'est pas trouvé
     */
    private void PAYLOAD(Message message) throws InterruptedException, NoSuchAlgorithmException {
        _data = message.get_arg1();
        System.out.println("Payload reçue");
        tryStartSolving();
    }

    /**
     * Traite la commande "YOU_DONT_FOOL_ME" en indiquant que la connexion est refusée en raison d'un mauvais mot de passe
     * et en arrêtant le programme.
     */
    private void YOU_DONT_FOOL_ME() {
        System.out.println("Connexion refusée (mauvais mot de passe)");
        System.exit(0);
    }

    /**
     * Traite la commande "PROGRESS" en envoyant l'état actuel de progression au serveur.
     */
    private void PROGRESS() {
        System.out.println("Progrès demandé");
        if (_data == null || _step == 0) {
            _outToServer.STATUS_IDLE();
            return;
        }

        if (_workerThread != null && _workerThread.isAlive()) {
            _outToServer.sendProgress(_tasker.getCurrentNonce());
            return;
        }

        _outToServer.STATUS_READY();
    }

    /**
     * Traite la commande "CANCELLED" en arrêtant le calcul en cours.
     *
     * @throws InterruptedException si l'opération est interrompue
     */
    private void CANCELLED() throws InterruptedException {
        _tasker.terminate();
        System.out.println("Minage arrêté par le serveur");
    }

    /**
     * Traite la commande "SOLVED" en indiquant qu'une solution a été trouvée par une autre machine
     * et en arrêtant le calcul en cours.
     *
     * @throws InterruptedException si l'opération est interrompue
     */
    private void SOLVED() throws InterruptedException {
        System.out.println("Solution trouvée par une autre machine");
        CANCELLED();
    }

    /**
     * Tente de démarrer le calcul dans in nouveau thread si toutes les conditions requises sont remplies.
     *
     * @throws InterruptedException si l'opération est interrompue
     * @throws NoSuchAlgorithmException si l'algorithme de hachage n'est pas trouvé
     */
    private void tryStartSolving() throws InterruptedException, NoSuchAlgorithmException {
        if (_data == null || _step == 0 || _difficulty == 0)
            return;
        if (_workerThread != null && _workerThread.isAlive())
            return;
        _tasker = new Task(_data, _difficulty, _startingNonce, _step, _outToServer);
        _workerThread = new Thread(_tasker);
        _workerThread.start();
    }
}
