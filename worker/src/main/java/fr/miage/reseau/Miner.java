package fr.miage.reseau;

public class Miner {
    private String _data;
    private int _difficulty;
    private Nonce _nonce;
    private int iterations;

    public Miner(String data, int difficulty, int step, int startingNounce) {
        setData(data);
        setDifficulty(difficulty);
        _nonce = new Nonce(1, 0);
        iterations = 0;
    }

    public void setData(String data) {
        _data = data;
    }

    public void setDifficulty(int difficulty) {
        _difficulty = Math.max(difficulty, 1);
    }

    public String getNonce() {
        return _nonce.toString();
    }

    public void computeNonce() {
        String preFix = getPrefix(_difficulty);
        while (!SHA256.getStringHash(_data + _nonce).startsWith(preFix)) {
            _nonce.Next();
            iterations++;
        }
    }

    public String getHash() {
        return SHA256.getStringHash(_data + _nonce);
    }

    public int getIterations() {
        return iterations;
    }

    private String getPrefix(int size) {
        StringBuilder preFix = new StringBuilder();
        for (int i = 0; i < size; i++) {
            preFix.append("0");
        }
        return preFix.toString();
    }
}