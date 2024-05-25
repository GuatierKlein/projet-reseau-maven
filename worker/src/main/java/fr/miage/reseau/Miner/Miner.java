package fr.miage.reseau.Miner;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * La classe Miner implémente un mécanisme de minage basé sur un algorithme de proof-of-work.
 */
public class Miner {
    private String _data;
    private byte[] _dataBytes;
    private int _difficulty;
    private Nonce _nonce;
    private long _iterations;
    private boolean _isWorking;
    private boolean _stop;

    /**
     * Constructeur de la classe Miner.
     *
     * @param data        les données à miner
     * @param difficulty  la difficulté du minage
     * @param step        l'incrément du nonce
     * @param startingNounce le nonce initial
     */
    public Miner(String data, int difficulty, int step, long startingNounce) {
        setData(data);
        setDifficulty(difficulty);
        _nonce = new Nonce(step, startingNounce);
        _iterations = 0;
    }

    /**
     * Définit les données à miner.
     *
     * @param data les nouvelles données
     */
    public void setData(String data) {
        _data = data;
        try {
            _dataBytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Définit la difficulté du minage.
     *
     * @param difficulty la nouvelle difficulté
     */
    public void setDifficulty(int difficulty) {
        _difficulty = Math.max(difficulty, 1);
    }

    /**
     * Retourne le nonce sous forme de chaîne de caractères.
     *
     * @return le nonce en chaîne de caractères
     */
    public String getNonce() {
        return _nonce.toString();
    }

    /**
     * Arrête le processus de minage.
     */
    public void stop() {
        _stop = true;
    }

    /**
     * Retourne le nonce sous forme de chaîne hexadécimale.
     *
     * @return le nonce en chaîne hexadécimale
     */
    public String getNonceHexString() {
        return _nonce.toHexString();
    }

    /**
     * Vérifie si le nonce actuel satisfait la difficulté requise.
     *
     * @return true si le nonce satisfait la difficulté, false sinon
     */
    public boolean didFind() {
        try {
            return SHA256.hashHasAtLeastXStartingZeroes(concatDataAndNounceBytes(), _difficulty);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lance le processus de calcul du nonce.
     */
    public void computeNonce() {
        _isWorking = true;
        _stop = false;
        try {
            while (!SHA256.hashHasAtLeastXStartingZeroes(concatDataAndNounceBytes(), _difficulty) && !_stop) {
                _nonce.Next();
                _iterations++;
                // log();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        _isWorking = false;
    }

    /**
     * Méthode de log pour déboguer le processus de minage.
     *
     * @throws NoSuchAlgorithmException si l'algorithme de hachage est introuvable
     */
    @SuppressWarnings("unused")
    private void log() throws NoSuchAlgorithmException {
        System.out.print("Résultat : ");
        System.out.println(getHash());
        System.out.print("Nonce Hex : ");
        System.out.println(getNonceHexString());
        System.out.print("Nonce String : ");
        System.out.println(getNonce());
        System.out.print("Itérations : ");
        System.err.println(getIterations());
        System.out.print("Data + nonce :");
        System.out.println(concatDataAndNounceString());
        System.out.print("Valide :");
        System.out.println(SHA256.hashHasAtLeastXStartingZeroes(concatDataAndNounceBytes(), _difficulty));
    }

    /**
     * Retourne le hachage des données concaténées avec le nonce.
     *
     * @return le hachage en chaîne de caractères
     * @throws NoSuchAlgorithmException si l'algorithme de hachage est introuvable
     */
    public String getHash() throws NoSuchAlgorithmException {
        return SHA256.getHash(concatDataAndNounceBytes());
    }

    /**
     * Retourne le nombre d'itérations effectuées lors du minage.
     *
     * @return le nombre d'itérations
     */
    public long getIterations() {
        return _iterations;
    }

    /**
     * Concatène les données et le nonce en un seul tableau de bytes.
     *
     * @return le tableau de bytes résultant
     */
    private byte[] concatDataAndNounceBytes() {
        byte[] nonce = _nonce.getBytes();
        int newLength = _dataBytes.length + nonce.length;

        byte[] res = new byte[newLength];

        System.arraycopy(_dataBytes, 0, res, 0, _dataBytes.length);
        System.arraycopy(nonce, 0, res, _dataBytes.length, nonce.length);

        return res;
    }

    /**
     * Concatène les données et le nonce en une seule chaîne de caractères.
     *
     * @return la chaîne de caractères résultante
     */
    private String concatDataAndNounceString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_data);
        sb.append(_nonce);
        return sb.toString();
    }

    /**
     * Vérifie si le processus de minage est en cours.
     *
     * @return true si le minage est en cours, false sinon
     */
    public boolean isWorking() {
        return _isWorking;
    }
}
