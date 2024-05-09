package fr.miage.reseau;

public class Miner {
    private String _data;
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
    }

    public void setDifficulty(int difficulty) {
        _difficulty = Math.max(difficulty, 1);
    }

    public String getNonce() {
        return _nonce.toString();
    }

    public void computeNonce() {
        while (!SHA256.hashHasAtLeastXStartingZeroes(concatDataAndNounce(), _difficulty)) {
            _nonce.Next();
            iterations++;
        }
    }

    public String getHash() {
        return SHA256.getStringHash(concatDataAndNounce());
    }

    public int getIterations() {
        return iterations;
    }

    private String concatDataAndNounce() {
        StringBuilder sb = new StringBuilder();
        sb.append(_data);
        sb.append(_nonce);
        return sb.toString();
    }
}