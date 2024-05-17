package fr.miage.reseau.Miner;

public class Main {
    public static void main(String[] args) throws Exception {
        testMiner();
    }
    public static void testMiner() throws Exception {
        String data = "uoazc55psst58zahut22rq19rjg0861og4fjnw6ldiqr7ncmxkq5";
        Miner miner = new Miner(data, 1, 1, 0);
        long startTime = System.nanoTime();
        miner.computeNonce();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        double milliseconds = duration / 1e6;
        System.out.println("FOUND!");
        System.out.print("Data : ");
        System.out.println(data);
        System.out.print("Résultat : ");
        System.out.println(miner.getHash());
        System.out.print("Nonce Hex : ");
        System.out.println(miner.getNonceHexString());
        System.out.print("Nonce String : ");
        System.out.println(miner.getNonce());
        System.out.print("Itérations : ");
        System.err.println(miner.getIterations());
        System.out.println("Durée du traitement : " + milliseconds + " millisecondes");
    }
}