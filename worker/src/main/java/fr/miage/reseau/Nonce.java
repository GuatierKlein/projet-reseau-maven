package fr.miage.reseau;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Nonce {
    private long _value;
    private int _step;

    public Nonce(int step, int initValue) {
        _step = step;
        _value = initValue;
    }

    public void Next() throws Exception {
        if(_value > Long.MAX_VALUE - _step) throw new Exception("Nonce maximum atteint");
        _value += _step;
    }

    public String toHexString() {
        return Long.toHexString(_value);
    }

    public String toString() {
        try {
            return new String(getBytes(), "UTF-8");
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(_value);
        return trimByteArray(buffer.array());
    }

    private byte[] trimByteArray(byte[] array) {
        int i = 0;
        while (i < array.length && array[i] == 0) {
            i++;
        }

        if(i == array.length)
            return new byte[] {0};

        int trimmedLength = array.length - i;
        byte[] trimmedArray = new byte[array.length - i];
        System.arraycopy(array, i, trimmedArray, 0, trimmedLength);

        return trimmedArray;
    }
}


