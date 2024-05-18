package fr.miage.reseau.Worker;

public class App {
    public static void main(String[] args) {
        if(args.length != 3) {
            System.out.println("Arguments invalides");
            return;
        }

        System.out.println("Demarrage du client...");
        Worker worker = new Worker(args[0], args[1], Integer.parseInt(args[2]));
        Thread readerThread = new Thread(worker);
        readerThread.start(); 
        System.out.println("Client demarre");
    }
}
