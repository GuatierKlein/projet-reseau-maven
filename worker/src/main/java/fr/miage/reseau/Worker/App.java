package fr.miage.reseau.Worker;

public class App {
    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Arguments invalides");
            return;
        }

        new Worker().launch(args[0], Integer.parseInt(args[1]));
    }
}
