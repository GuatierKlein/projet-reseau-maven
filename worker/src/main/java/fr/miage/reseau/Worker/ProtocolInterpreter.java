package fr.miage.reseau.Worker;

public class ProtocolInterpreter {
    // private MessageLine message;
    private String password;
    private Thread workerThread;

    public ProtocolInterpreter() {}

    public void setPassword(String password) {
        this.password = password;
    }

    public void execute(MessageLine message) {
        switch (message.getCommand()) {
            case "WHO_ARE_YOU_?":    
            break;
            case "GIMME_PASSWORD":
            break; 
            case "HELLO_YOU":
            break;
            case "OK":
            break;
            case "NONCE":
            break;
            case "PAYLOAD":
            break; 
            case "SOLVE":
            break;
            case "YOU_DONT_FOOL_ME":
            break;
            case "PROGRESS":
            break;
            case "CANCELLED":
            break;
            case "SOLVED":
            break;
        
            default:
                break;
        }
    }

    private void SOLVE() {
        workerThread = new Thread();
    }

    private void WHO_ARE_YOU() {

    }

    private void GIMME_PASSWORD() {

    }

    private void HELLO_WORLD() {

    }

    private void OK() {

    }

    private void NONCE() {

    }

    private void PAYLOAD() {

    }

    private void YOU_DONT_FOOL_ME() {

    }

    private void PROGRESS() {

    }

    private void CANCELLED() {

    }

    private void SOLVED() {

    }
}
