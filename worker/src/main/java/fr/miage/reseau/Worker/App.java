package fr.miage.reseau.Worker;

public class App {
    public static void main(String[] args) { //serverAddress, password, port
        if(args.length != 3) {
            System.out.println("Arguments invalides");
            return;
        }

        System.out.println("Demarrage du client...");
        new Worker(args[0], args[1], Integer.parseInt(args[2])).run();;
        System.out.println("CLient arrete");
    }
}
