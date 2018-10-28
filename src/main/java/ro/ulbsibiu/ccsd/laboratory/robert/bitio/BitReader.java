package ro.ulbsibiu.ccsd.laboratory.robert.bitio;

import java.io.IOException;
import java.io.InputStream;

public class BitReader {
    private InputStream inputStream;
    private ReadBuffer buffer = new ReadBuffer();

    public BitReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public boolean readBit() throws IOException {
        boolean bit;
        try {
            bit = buffer.nextBit();
        } catch (EmptyBufferException ebe) {
            buffer.refill((byte) inputStream.read());
            bit = buffer.nextBit();
        }
        return bit;
    }

    public boolean[] readNBits(int nrBits) throws IOException {
        boolean[] bits = new boolean[nrBits];
        for (int i = 0; i < nrBits; i++) {
            bits[i] = readBit();
        }
        return bits;
    }
}
