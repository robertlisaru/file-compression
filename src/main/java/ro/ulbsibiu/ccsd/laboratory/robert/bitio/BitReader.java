package ro.ulbsibiu.ccsd.laboratory.robert.bitio;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.exception.EmptyBufferException;

import java.io.IOException;
import java.io.InputStream;

public class BitReader {
    private InputStream inputStream;
    private ReadBuffer buffer = new ReadBuffer();

    public BitReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public boolean readBitAsBoolean() throws IOException {
        boolean bit;
        try {
            bit = buffer.nextBit();
        } catch (EmptyBufferException ebe) {
            buffer.refill((byte) inputStream.read());
            bit = buffer.nextBit();
        }
        return bit;
    }

    public int readBitAsInt() throws IOException {
        return readBitAsBoolean() ? 1 : 0;
    }

    public boolean[] readNBitsAsBooleanArray(int nrBits) throws IOException {
        boolean[] bits = new boolean[nrBits];
        for (int i = 0; i < nrBits; i++) {
            bits[i] = readBitAsBoolean();
        }
        return bits;
    }

    public long readNBitValue(int n) throws IOException {
        long nBitValue = 0;
        for (int i = 0; i < n; i++) {
            nBitValue |= (readBitAsInt() << i);
        }
        return nBitValue;
    }
}
