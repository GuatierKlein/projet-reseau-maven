package fr.miage.reseau.Miner;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * La classe Nonce gère le nonce utilisé pour le processus de minage.
 * Elle peut fonctionner avec des valeurs de type long ou BigInteger lorsque la limite de long est dépassée.
 */
public class Nonce {
    private long _value;
    private BigInteger _altValue;
    private int _step;
    private boolean _useAltValue = false;

    /**
     * Constructeur de la classe Nonce.
     *
     * @param step      l'incrément du nonce
     * @param initValue la valeur initiale du nonce
     */
    public Nonce(int step, long initValue) {
        _step = step;
        _value = initValue;
    }

    /**
     * Incrémente le nonce de la valeur de l'étape.
     * Passe à l'utilisation de BigInteger si la valeur maximale de long est atteinte.
     *
     * @throws Exception si une erreur se produit lors de l'incrémentation
     */
    public void Next() throws Exception {
        if (_useAltValue) {
            _altValue = _altValue.add(BigInteger.valueOf(_step));
            return;
        }

        if (_value > Long.MAX_VALUE - _step && !_useAltValue) {
            _altValue = BigInteger.valueOf(_value).add(BigInteger.valueOf(_step));
            _useAltValue = true;
            System.out.println("Nonce max atteint, passage au BigInteger");
            return;
        }
        _value += _step;
    }

    /**
     * Retourne le nonce sous forme de chaîne hexadécimale.
     *
     * @return le nonce en chaîne hexadécimale
     */
    public String toHexString() {
        if (!_useAltValue) {
            return Long.toHexString(_value);
        }
        return _altValue.toString(16);
    }

    /**
     * Retourne le nonce sous forme de chaîne de caractères en utilisant l'encodage UTF-8.
     *
     * @return le nonce en chaîne de caractères
     */
    public String toString() {
        try {
            return new String(getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Retourne le nonce sous forme de tableau de bytes.
     *
     * @return le nonce en tableau de bytes
     */
    public byte[] getBytes() {
        if (!_useAltValue) {
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.putLong(_value);
            return trimByteArray(buffer.array());
        }
        return trimByteArray(_altValue.toByteArray());
    }

    /**
     * Supprime les octets initiaux nuls du tableau de bytes fourni.
     *
     * @param array le tableau de bytes à ajuster
     * @return le tableau de bytes ajusté sans les zéros initiaux
     */
    private byte[] trimByteArray(byte[] array) {
        int i = 0;
        while (i < array.length && array[i] == 0) {
            i++;
        }

        if (i == array.length) {
            return new byte[]{0};
        }

        int trimmedLength = array.length - i;
        byte[] trimmedArray = new byte[trimmedLength];
        System.arraycopy(array, i, trimmedArray, 0, trimmedLength);

        return trimmedArray;
    }
}
