package ro.ulbsibiu.ccsd.laboratory.robert.encryption.huffman;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;

import java.io.IOException;

public class HuffmanFileHeaderReader extends HuffmanFileHeader {
    public HuffmanFileHeader.CounterSize[] readCounterSizes(BitReader bitReader) throws IOException {
        counterSizes = new CounterSize[256];
        for (int i = 0; i < 256; i++) {
            counterSizes[i] = CounterSize.values()[(int) bitReader.readNBitValue(2)];
        }
        return counterSizes;
    }

    public int[] readHistogram(BitReader bitReader) throws IOException {
        if (counterSizes == null) {
            throw new RuntimeException("Counter sizes not initialized");
        }
        histogram = new int[256];
        for (int i = 0; i < 256; i++) {
            if (counterSizes[i] != CounterSize.NU_SE_REPREZINTA) {
                histogram[i] = (int) bitReader.readNBitValue(counterSizes[i].getNrBytes() * 8);
            }
        }
        return histogram;
    }
}
