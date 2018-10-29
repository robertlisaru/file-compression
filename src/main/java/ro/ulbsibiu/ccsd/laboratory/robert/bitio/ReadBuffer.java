package ro.ulbsibiu.ccsd.laboratory.robert.bitio;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.exception.EmptyBufferException;

import java.util.Arrays;

public class ReadBuffer {
    private boolean[] bits = new boolean[8];
    private int pos = 8;

    public int length() {
        return 8 - pos;
    }

    /**
     * @param value the bits to fill the buffer with
     */
    public void refill(byte value) {
        for (int i = 0; i < 8; i++) {
            bits[i] = (value & (1 << i)) == (1 << i);
        }
        pos = 0;
    }

    public boolean isEmpty() {
        return pos == 8;
    }

    /**
     * @return the next bit in the buffer represented as boolean, bits are fetched starting with LSB
     * @throws EmptyBufferException when the buffer is empty
     */
    public boolean nextBit() {
        if (isEmpty()) {
            throw new EmptyBufferException("Cannot read from empty buffer");
        }
        return bits[pos++];
    }

    /**
     * @return all the remaining bits in the buffer
     */
    public boolean[] remainingBits() {
        boolean[] remaining = Arrays.copyOfRange(bits, pos, 8);
        pos = 8;
        return remaining;
    }
}
