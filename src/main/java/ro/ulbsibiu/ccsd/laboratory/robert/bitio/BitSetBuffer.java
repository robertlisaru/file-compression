package ro.ulbsibiu.ccsd.laboratory.robert.bitio;

import java.util.BitSet;

import static ro.ulbsibiu.ccsd.laboratory.robert.util.TypeConvertor.asBitSet;

public class BitSetBuffer {
    private final int size;
    private BitSet buffer;
    private int pos = 0;

    /**
     * Creates a BitSetBuffer backed by a BitSet of the specified size.
     *
     * @param size maximum number of bits the buffer can hold
     */
    public BitSetBuffer(int size) {
        this.size = size;
        buffer = new BitSet(size);
    }

    /**
     * @param value the bits to fill the buffer with
     */
    public void refill(byte value) {
        buffer = asBitSet(value);
        pos = 0;
    }

    public boolean isEmpty() {
        return pos == size;
    }

    /**
     * @return the next bit in the buffer represented as boolean
     * @throws EmptyBufferException when the buffer is empty
     */
    public boolean nextBit() {
        if (isEmpty()) {
            throw new EmptyBufferException("Cannot read from empty buffer");
        }
        return buffer.get(pos++);
    }

    /**
     * @return all the remaining bits in the buffer represented as BitSet
     */
    public BitSet remaining() {
        return buffer.get(pos, size);
    }
}
