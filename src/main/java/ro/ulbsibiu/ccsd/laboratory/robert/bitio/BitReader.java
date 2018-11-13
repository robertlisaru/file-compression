package ro.ulbsibiu.ccsd.laboratory.robert.bitio;

import java.io.IOException;
import java.io.InputStream;

public class BitReader {
    private InputStream inputStream;
    private ReadBuffer buffer = new ReadBuffer();

    public BitReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int readBit() throws IOException {
        if (buffer.isEmpty()) {
            int readByte = inputStream.read();
            if (readByte == -1) {
                throw new UnsupportedOperationException("Cannot read bit, stream ended");
            }
            buffer.refill((byte) readByte);
        }
        return buffer.nextBit();
    }

    public long readNBitValue(int n) throws IOException {
        long nBitValue = 0;
        for (int i = 0; i < n; i++) {
            nBitValue |= (readBit() << i);
        }
        return nBitValue;
    }
}
