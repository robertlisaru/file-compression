package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.encoder;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LZWEncoder {
    private BitWriter bitWriter;
    private InputStream inputStream;
    private Dictionary dictionary;
    private int numBitsForIndex;
    private int previousReadByte;

    public LZWEncoder(int dictinoaryType, int numBitsForIndex, InputStream inputStream, OutputStream outputStream) {
        bitWriter = new BitWriter(outputStream);
        this.inputStream = inputStream;
        dictionary = dictinoaryType == 0 ? new FreezeStrategyDictionary(numBitsForIndex)
                : new EmptyStrategyDictionary(numBitsForIndex);
        this.numBitsForIndex = numBitsForIndex;
    }

    public void writeHeader() throws IOException {
        bitWriter.writeBit((int) dictionary.getHeaderCode());
        bitWriter.writeNBitValue(numBitsForIndex, 4);
        previousReadByte = inputStream.read();
    }

    public void encode() throws IOException {
        bitWriter.writeBit((int) dictionary.getHeaderCode());
        bitWriter.writeNBitValue(numBitsForIndex, 4);
        int index;
        int readByte = inputStream.read();
        do {
            int prefix = Dictionary.EMPTY_PREFIX;
            while (readByte != -1
                    && (index = dictionary.search(prefix, (byte) readByte)) != Dictionary.NULL_INDEX) {
                prefix = index;
                readByte = inputStream.read();
            }
            bitWriter.writeNBitValue(prefix, numBitsForIndex);
        } while (readByte != -1);
        bitWriter.flush();
    }

    public long nextIndex() throws IOException {
        if (previousReadByte == -1) {
            return -1;
        }
        int index;
        int prefix = Dictionary.EMPTY_PREFIX;
        while (previousReadByte != -1
                && (index = dictionary.search(prefix, (byte) previousReadByte)) != Dictionary.NULL_INDEX) {
            prefix = index;
            previousReadByte = inputStream.read();
        }
        bitWriter.writeNBitValue(prefix, numBitsForIndex);
        return prefix;
    }

    public void flush() throws IOException {
        bitWriter.flush();
    }
}
