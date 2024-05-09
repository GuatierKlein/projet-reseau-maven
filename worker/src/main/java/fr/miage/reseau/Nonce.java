package fr.miage.reseau;

public class Nonce {
    private long _value;
    private int _clientIndex;

    public Nonce(int step, int initValue) {
        _clientIndex = step;
        _value = initValue;
    }

    public void Next() {
        _value += _clientIndex;
    }

    public String toString() {
        return Long.toHexString(_value).toUpperCase();
    }
}

