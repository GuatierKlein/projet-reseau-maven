package fr.miage.reseau.Miner;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * La classe SHA256 fournit des méthodes utilitaires pour générer des hachages SHA-256 et vérifier des conditions spécifiques sur les hachages.
 */
public class SHA256 {

    /**
     * Génère le hachage SHA-256 d'une chaîne de caractères et le retourne sous forme de chaîne hexadécimale.
     *
     * @param input la chaîne de caractères à hacher
     * @return le hachage SHA-256 sous forme de chaîne hexadécimale
     * @throws UnsupportedEncodingException si l'encodage UTF-8 n'est pas supporté
     * @throws NoSuchAlgorithmException si l'algorithme SHA-256 n'est pas disponible
     */
    public static String getHash(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return encodeHex(getDigest(input));
    }  

    /**
     * Génère le hachage SHA-256 d'un tableau de bytes et le retourne sous forme de chaîne hexadécimale.
     *
     * @param input le tableau de bytes à hacher
     * @return le hachage SHA-256 sous forme de chaîne hexadécimale
     * @throws NoSuchAlgorithmException si l'algorithme SHA-256 n'est pas disponible
     */
    public static String getHash(byte[] input) throws NoSuchAlgorithmException {
        return encodeHex(getDigest(input));
    }  

    /**
     * Vérifie si le hachage SHA-256 d'une chaîne de caractères commence par au moins x zéros.
     *
     * @param input la chaîne de caractères à hacher
     * @param x le nombre de zéros requis au début du hachage
     * @return true si le hachage commence par au moins x zéros, false sinon
     * @throws UnsupportedEncodingException si l'encodage UTF-8 n'est pas supporté
     * @throws NoSuchAlgorithmException si l'algorithme SHA-256 n'est pas disponible
     */
    public static boolean hashHasAtLeastXStartingZeroes(String input, int x) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return countStartingZeroesInHash(getDigest(input)) >= x;
    }

    /**
     * Vérifie si le hachage SHA-256 d'un tableau de bytes commence par au moins x zéros.
     *
     * @param input le tableau de bytes à hacher
     * @param x le nombre de zéros requis au début du hachage
     * @return true si le hachage commence par au moins x zéros, false sinon
     * @throws NoSuchAlgorithmException si l'algorithme SHA-256 n'est pas disponible
     */
    public static boolean hashHasAtLeastXStartingZeroes(byte[] input, int x) throws NoSuchAlgorithmException {
        return countStartingZeroesInHash(getDigest(input)) >= x;
    }

    /**
     * Convertit un tableau de bytes en une chaîne hexadécimale.
     *
     * @param digest le tableau de bytes à convertir
     * @return la chaîne hexadécimale représentant le tableau de bytes
     */
    private static String encodeHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Génère le hachage SHA-256 d'une chaîne de caractères.
     *
     * @param input la chaîne de caractères à hacher
     * @return le hachage SHA-256 sous forme de tableau de bytes
     * @throws UnsupportedEncodingException si l'encodage UTF-8 n'est pas supporté
     * @throws NoSuchAlgorithmException si l'algorithme SHA-256 n'est pas disponible
     */
    private static byte[] getDigest(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] buffer = input.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();
        return digest;
    }

    /**
     * Génère le hachage SHA-256 d'un tableau de bytes.
     *
     * @param input le tableau de bytes à hacher
     * @return le hachage SHA-256 sous forme de tableau de bytes
     * @throws NoSuchAlgorithmException si l'algorithme SHA-256 n'est pas disponible
     */
    private static byte[] getDigest(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input);
        byte[] digest = md.digest();
        return digest;
    }

    /**
     * Compte le nombre de zéros au début d'un hachage.
     *
     * @param hash le hachage à analyser
     * @return le nombre de zéros au début du hachage
     */
    private static int countStartingZeroesInHash(byte[] hash) {
        int res = 0;
        for (int i = 0; i < hash.length; i++) {
            int zeroCountInCurrentByte = countStartingZeroesInByte(hash[i]);
            res += zeroCountInCurrentByte;
            if (zeroCountInCurrentByte < 2) 
                return res;
        }
        return res;
    }

    /**
     * Compte le nombre de zéros au début d'un octet.
     *
     * @param value l'octet à analyser
     * @return le nombre de zéros au début de l'octet
     */
    private static int countStartingZeroesInByte(byte value) {
        if ((value & 0xff) == 0x0) 
            return 2;
        if ((value & 0xff) <= 0x0f) 
            return 1;
        return 0;
    }
}
