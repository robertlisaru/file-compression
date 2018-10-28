package ro.ulbsibiu.ccsd.laboratory.robert.bitio;

import java.io.IOException;
import java.io.OutputStream;

public class BitWriter {
    private OutputStream outputStream;
    private WriteBuffer buffer = new WriteBuffer();

    public BitWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeBit(boolean bit) throws IOException {
        try {
            buffer.putBit(bit);
        } catch (FullBufferException fbe) {
            outputStream.write(buffer.getByteAndClear());
            buffer.putBit(bit);
        }
    }

    public void writeNBits(int nrBits, boolean[] bits) throws IOException {
        for (int i = 0; i < nrBits; i++) {
            writeBit(bits[i]);
        }
    }

    public void flush() throws IOException {
        outputStream.write(buffer.getByteAndClear());
    }
}
