package fr.ul.miage.reseau.mv;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client{

    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 1337;
    
    // private static boolean isMining = true;
    // private static String pwd ="test";
    // private static String actualNonce ="1";
    // private static boolean sendSolution = false;
	
    //////////////////////////////////MAIN///////////////////////////////////////////////////////////
    
    // main avec input de l'utilisateur
    
    public static void main(String[] args) {

        String userInput;
        boolean isMining = false;

        // Connexion au serveur
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String serverMessage;
            Scanner scanner = new Scanner(System.in);

            while ((serverMessage = reader.readLine()) != null) {
                System.out.println(serverMessage);

                if (serverMessage.equals("WHO_ARE_YOU_?")) {
                    // Attendre la réponse appropriée 
                    while (true) {
                        userInput = scanner.nextLine();
                        if (userInput.trim().equals(respondToWhoAreYou())) {
                            writer.println(respondToWhoAreYou());
                            break;
                        }
                    }
                } else if (serverMessage.equals("GIMME_PASSWORD")) {
                    // Attendre un mdp valide au bon format
                    while (true) {
                        userInput = scanner.nextLine();
                        if (userInput.matches("^PASSWD .+$")) {
                            writer.println(userInput);
                            break;
                        }
                    }
                } else if (serverMessage.equals("HELLO_YOU") || serverMessage.equals("SOLVED") || serverMessage.equals("CANCELLED")) {
                    // Attendre la réponse appropriée
                    while (true) {
                        userInput = scanner.nextLine();
                        if (userInput.trim().equals(indicateReady())) {
                            writer.println(indicateReady());
                            break;
                        }
                    }
                } else if (serverMessage.equals("PROGRESS")) {
                    // Attendre la réponse appropriée
                    while (true) {
                        userInput = scanner.nextLine();
                        if (userInput.equals(indicateWorkerStatus(isMining))) {
                            writer.println(indicateWorkerStatus(isMining));
                            break;
                        }
                    }
                    
                    // Attendre l'envoi du hash trouvé au bon format
                    while (isMining) {
                        userInput = scanner.nextLine();
                        if (userInput.matches("^FOUND \\w+ \\d+$")) {
                            writer.println(userInput);
                            break;
                        }
                    }
                    
                }
            }

            //Deconnexion du serveur
            socket.close();

        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion au serveur : " + e.getMessage());
        }
    }


    // main avec saisie automatique
    
    /*public static void main(String[] args) {

        // Connexion au serveur
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

        	 String serverMessage;
             while ((serverMessage = reader.readLine()) != null) {
                System.out.println("Instruction reçue du serveur : " + serverMessage); // Afficher les instructions du serveur dans le terminal
                if (serverMessage.equals("WHO_ARE_YOU_?")) {
                    // Envoyer des messages au serveur
                    writer.println(respondToWhoAreYou());
                }
                if (serverMessage.equals("GIMME_PASSWORD")){
                    writer.println(providePassword(pwd)); //Récupérer le password aléatoire envoyé par le serveur (cf Marcus)
                    // Envoyer mdp au serveur
                }
                if (serverMessage.equals("HELLO_YOU") || serverMessage.equals("SOLVED") || serverMessage.equals("CANCELLED")){
                    writer.println(indicateReady());
                    // Indiquer au serveur qu'on est prêt à commencer une tâche ou pour la suivante si celle en cours vient d'être achevée ou annulée
                }
                if (serverMessage.equals("PROGRESS")){
                    writer.println(indicateWorkerStatus(isMining, actualNonce)); //Récupérer le nonce du worker (cf Gautier)
                    //test envoi solution
                    sendSolution = true;
                    // Indiquer au serveur le statut du worker
                }
                if (sendSolution) {
                    //Envoyer la solution
                    writer.println(provideSolution("0c4f12188163dae848bd233757f3b0966972dd9efcaa54af4de92dfceb2c755e","1"));
                    sendSolution = false; // Réinitialisation du booléen
                }
            }

            //Deconnexion du serveur
            socket.close();

        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion au serveur : " + e.getMessage());
        }
    }*/
    
//////////////////////////////METHODE POUR TEST//////////////////////////////////////////////////
    
	// Méthode pour répondre à "WHO_ARE_YOU_?"
	public static String respondToWhoAreYou() {
	return "ITS_ME";
	}
	
	// Méthode pour répondre à "GIMME_PASSWORD"
	public static String providePassword(String password) {
	return "PASSWD " + password;
	}
	
	// Méthode pour indiquer au serveur que le worker est prêt
	public static String indicateReady() {
	return "READY";
	}
	
	// Méthode pour indiquer au serveur que le worker est prêt
	public static String indicateWorkerStatus(boolean isMining) {
	return isMining ? "TESING" + " " +getActualNonce() : "NOPE";
	}

    // Méthode pour envoyer au serveur le nonce actuel
	public static int getActualNonce() {
        return 1;
        }
	
	// Méthode pour indiquer au serveur qu'une solution a été trouvée
	public static String provideSolution(String hash, String nonce) {
		return "FOUND " + hash + " " + nonce;
	}
}
