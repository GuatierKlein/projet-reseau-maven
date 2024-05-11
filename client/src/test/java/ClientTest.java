import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientTest {

    private static final int PORT = 1337;

    
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré sur le port " + PORT);

            while (true) {
                // Accepter la connexion du client
                Socket clientSocket = serverSocket.accept();
                // Interroger le client
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                output.println("WHO_ARE_YOU_?");
                // Lire les messages du client
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String clientMessage;
                while ((clientMessage = reader.readLine()) != null) {
                    System.out.println("Message reçu du client : " + clientMessage);
                    if (clientMessage.equals("ITS_ME")) {
                        output.println("GIMME_PASSWORD");
                        break;
                    }
                }

                String clientPassword;
                while ((clientPassword = reader.readLine()) != null) {
                    System.out.println("Message reçu du client : " + clientPassword);
                    if (clientPassword.startsWith("PASSWD")) {
                        output.println("HELLO_YOU");
                        break;
                    }
                }

                String clientStatus;
                while ((clientStatus = reader.readLine()) != null) {
                    System.out.println("Message reçu du client : " + clientStatus);
                    if (clientStatus.equals("READY")) {
                        output.println("PROGRESS");
                        break;
                    }
                }
                
                String workerStatus;
                while ((workerStatus = reader.readLine()) != null) {
                    System.out.println("Message reçu du client : " + workerStatus);
                }
                
                String foundSolution;
                while ((foundSolution = reader.readLine()) != null) {
                    System.out.println("Message reçu du client : " + foundSolution);
                }

                // Fermer le socket du client
                clientSocket.close();
                System.out.println("Connexion client fermée.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la gestion du serveur : " + e.getMessage());
        }
    }
}

