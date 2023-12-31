package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.huffmanstatic;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;

import java.io.IOException;

public class HuffmanStaticFileHeaderReader extends HuffmanStaticFileHeaderProcessor {
    public CounterLength[] readCounterSizes(BitReader bitReader) throws IOException {
        counterCodes = new CounterLength[256];
        for (int i = 0; i < 256; i++) {
            counterCodes[i] = CounterLength.values()[(int) bitReader.readNBitValue(2)];
        }
        return counterCodes;
    }

    public int[] readHistogram(BitReader bitReader) throws IOException {
        if (counterCodes == null) {
            throw new RuntimeException("Counter sizes not initialized");
        }
        histogram = new int[256];
        for (int i = 0; i < 256; i++) {
            if (counterCodes[i] != CounterLength.NU_SE_REPREZINTA) {
                histogram[i] = (int) bitReader.readNBitValue(counterCodes[i].getNrBytes() * 8);
            }
        }
        return histogram;
    }

    public int getTotalByteCount() {
        if (histogram == null) {
            throw new RuntimeException("Histogram is null. Cannot compute total byte count.");
        }
        int sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += histogram[i];
        }
        return sum;
    }
}
