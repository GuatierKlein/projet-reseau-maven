package fr.miage.reseau.Miner;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class Nonce {
    private long _value;
    private BigInteger _altValue;
    private int _step;
    private boolean _useAltValue = false;

    public Nonce(int step, long initValue) {
        _step = step;
        _value = initValue;
    }

    public void Next() throws Exception {
        if(_useAltValue) {
            _altValue.add(BigInteger.valueOf(_step));
            return;
        }

        if(_value > Long.MAX_VALUE - _step && !_useAltValue) {
            _altValue = BigInteger.valueOf(_value);
            _altValue.add(BigInteger.valueOf(_step));
            _useAltValue = true;
            System.out.println("Nonce max atteint, passage au Biginteger");
            return;
        }
        _value += _step;
    }

    public String toHexString() {
        if(!_useAltValue)
            return Long.toHexString(_value);

        return _altValue.toString(16);
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
        if(!_useAltValue) {
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.putLong(_value);
            return trimByteArray(buffer.array());
        }

        return trimByteArray(_altValue.toByteArray());
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


