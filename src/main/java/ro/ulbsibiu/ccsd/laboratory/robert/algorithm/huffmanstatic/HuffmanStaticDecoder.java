package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.huffmanstatic;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HuffmanStaticDecoder extends HuffmanStatic {
    private final BitReader bitReader;
    private Map<Integer, Integer> hashMap = new HashMap<>();

    public HuffmanStaticDecoder(BitReader bitReader) {
        super(new HuffmanStaticFileHeaderReader());
        this.bitReader = bitReader;
    }

    public void readHeader() throws IOException {
        ((HuffmanStaticFileHeaderReader) fileHeaderProcessor).readCounterSizes(bitReader);
        ((HuffmanStaticFileHeaderReader) fileHeaderProcessor).readHistogram(bitReader);
    }

    private void buildHashMap() {
        for (int i = 0; i < 256; i++) {
            if (dictionary[i].getSizeInBits() != 0) {
                hashMap.put((dictionary[i].getCode() << 8) | dictionary[i].getSizeInBits(), i);
            }
        }
    }

    public void decodeAndWrite(OutputStream outputStream) throws IOException {
        buildHashMap();
        int totalByteCount = ((HuffmanStaticFileHeaderReader) fileHeaderProcessor)
                .getTotalByteCount();
        for (int i = 0; i < totalByteCount; i++) {
            int code = 0;
            int sizeInBits = 0;
            do {
                code = code | (bitReader.readBit() << sizeInBits);
                sizeInBits++;
            } while (!hashMap.containsKey((code << 8) | sizeInBits));
            outputStream.write(hashMap.get((code << 8) | sizeInBits));
        }
    }
}
