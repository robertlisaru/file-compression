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
        if (buffer.isFull()) {
            outputStream.write(buffer.getByteAndClear());
        }
        buffer.putBit(bit);
    }

    public void writeBit(int bit) throws IOException {
        writeBit(bit != 0);
    }

    public void writeNBits(int nrBits, boolean[] bits) throws IOException {
        for (int i = 0; i < nrBits; i++) {
            writeBit(bits[i]);
        }
    }

    public void writeNBitValue(long value, int n) throws IOException {
        for (int i = 0; i < n; i++) {
            writeBit((int) ((value >> i) & 1));
        }
    }

    public void flush() throws IOException {
        outputStream.write(buffer.getByteAndClear());
        outputStream.flush();
    }
}
