package ro.ulbsibiu.ccsd.laboratory.robert.encoding.lzw;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LZWEncoder {
    private BitWriter bitWriter;
    private InputStream inputStream;
    private Dictionary dictionary;
    private int numBitsForIndex;

    public LZWEncoder(int numBitsForIndex, InputStream inputStream, OutputStream outputStream) {
        bitWriter = new BitWriter(outputStream);
        this.inputStream = inputStream;
        dictionary = new FreezeStrategyDictionary(numBitsForIndex);
        this.numBitsForIndex = numBitsForIndex;
    }

    public void encode() throws IOException {
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
}
