package org.rudogma.bytownite.utils;

import java.io.OutputStream;

public class UnsyncByteArrayOutputStream extends OutputStream {

    private static final int INITIAL_SIZE = 8192;

    byte[] buf;
    public int size = 0;
    public int pos = 0;

    public UnsyncByteArrayOutputStream() {
        this(INITIAL_SIZE);
    }

    public UnsyncByteArrayOutputStream(int size){
        buf = new byte[size];
        this.size = size;
    }

    public void write(byte[] arg0) {
        if ((Integer.MAX_VALUE - pos) < arg0.length) {
            throw new OutOfMemoryError();
        }
        int newPos = pos + arg0.length;
        if (newPos > size) {
            expandSize(newPos);
        }
        System.arraycopy(arg0, 0, buf, pos, arg0.length);
        pos = newPos;
    }

    public void write(byte[] arg0, int arg1, int arg2) {
        if ((Integer.MAX_VALUE - pos) < arg2) {
            throw new OutOfMemoryError();
        }
        int newPos = pos + arg2;
        if (newPos > size) {
            expandSize(newPos);
        }
        System.arraycopy(arg0, arg1, buf, pos, arg2);
        pos = newPos;
    }

    public void write(int arg0) {
        if ((Integer.MAX_VALUE - pos) == 0) {
            throw new OutOfMemoryError();
        }
        int newPos = pos + 1;
        if (newPos > size) {
            expandSize(newPos);
        }
        buf[pos++] = (byte)arg0;
    }

    public byte[] toByteArray() {
        byte result[] = new byte[pos];
        System.arraycopy(buf, 0, result, 0, pos);
        return result;
    }

    public void reset() {
        pos = 0;
    }

    private void expandSize(int newPos) {
        int newSize = size;
        while (newPos > newSize) {
            newSize = newSize << 1;
            // Deal with overflow
            if (newSize < 0) {
                newSize = Integer.MAX_VALUE;
            }
        }
        byte newBuf[] = new byte[newSize];
        System.arraycopy(buf, 0, newBuf, 0, pos);
        buf = newBuf;
        size = newSize;
    }
}
