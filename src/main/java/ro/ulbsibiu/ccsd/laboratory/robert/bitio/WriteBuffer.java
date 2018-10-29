package ro.ulbsibiu.ccsd.laboratory.robert.bitio;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.exception.FullBufferException;

public class WriteBuffer {
    private boolean[] bits = new boolean[8];
    private int pos = 0;

    public void putBit(boolean bit) {
        if (isFull()) {
            throw new FullBufferException("Cannot put a bit into a full buffer");
        }
        bits[pos++] = bit;
    }

    public boolean isFull() {
        return pos == 8;
    }

    public int length() {
        return pos;
    }

    public byte getByteAndClear() {
        byte theByte = 0;
        for (int i = 0; i < pos; i++) {
            if (bits[i]) {
                theByte |= (1 << i);
            }
        }
        pos = 0;
        return theByte;
    }
}
