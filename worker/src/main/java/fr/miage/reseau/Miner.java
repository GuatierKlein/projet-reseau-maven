package fr.miage.reseau;

import java.io.UnsupportedEncodingException;

public class Miner {
    private String _data;
    private byte[] _dataBytes;
    private int _difficulty;
    private Nonce _nonce;
    private int iterations;

    public Miner(String data, int difficulty, int step, int startingNounce) {
        setData(data);
        setDifficulty(difficulty);
        _nonce = new Nonce(step, startingNounce);
        iterations = 0;
    }

    public void setData(String data) {
        _data = data;
        try {
            _dataBytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setDifficulty(int difficulty) {
        _difficulty = Math.max(difficulty, 1);
    }

    public String getNonce() {
        return _nonce.toString();
    }

    public String getNonceHexString() {
        return _nonce.toHexString();
    }

    public void computeNonce() {
        while (!SHA256.hashHasAtLeastXStartingZeroes(concatDataAndNounceBytes(), _difficulty)) {
            _nonce.Next();
            iterations++;
            // System.out.print("Résultat : ");
            // System.out.println(getHash());
            // System.out.print("Nonce Hex : ");
            // System.out.println(getNonceHexString());
            // System.out.print("Nonce String : ");
            // System.out.println(getNonce());
            // System.out.print("Itérations : ");
            // System.err.println(getIterations());
            // System.out.print("Data + nonce :");
            // System.out.println(concatDataAndNounceString());
            // System.out.println(SHA256.hashHasAtLeastXStartingZeroes(concatDataAndNounceString(), _difficulty));
        }
    }

    public String getHash() {
        return SHA256.getStringHash(concatDataAndNounceString());
    }

    public int getIterations() {
        return iterations;
    }

    private byte[] concatDataAndNounceBytes() {
        byte[] nonce = _nonce.getBytes();
        int newLength = _dataBytes.length + nonce.length;

        byte[] res = new byte[newLength];

        System.arraycopy(_dataBytes, 0, res, 0, _dataBytes.length);
        System.arraycopy(nonce, 0, res, _dataBytes.length, nonce.length);

        return res;
    }

    private String concatDataAndNounceString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_data);
        sb.append(_nonce);
        return sb.toString();
    }
}