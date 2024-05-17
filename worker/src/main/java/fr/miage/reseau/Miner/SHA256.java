package fr.miage.reseau.Miner;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static String getStringHash(String input) {
        return encodeHex(getDigest(input));
    }  

    public static boolean hashHasAtLeastXStartingZeroes(String input, int x) {
        return countStartingZeroesInHash(getDigest(input)) >= x;
    }

    public static boolean hashHasAtLeastXStartingZeroes(byte[] input, int x) {
        return countStartingZeroesInHash(getDigest(input)) >= x;
    }

    private static String encodeHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    private static byte[] getDigest(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] buffer = input.getBytes("UTF-8");
            md.update(buffer);
            byte[] digest = md.digest();
            return digest;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private static byte[] getDigest(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input);
            byte[] digest = md.digest();
            return digest;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private static int countStartingZeroesInHash(byte[] hash) {
        int res = 0;
        for (int i = 0; i < hash.length; i++) {
            int zeroCountInCurrentByte = countStartingZeroesInByte(hash[i]);
            if(zeroCountInCurrentByte == 0) break;
            res += zeroCountInCurrentByte;
        }

        return res;
    }

    private static int countStartingZeroesInByte(byte value) {
        if((value & 0xff) == 0x0) return 2;
        if((value & 0xff) <= 0x0f) return 1;
        return 0;
    }
}
