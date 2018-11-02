package ro.ulbsibiu.ccsd.laboratory.robert.encryption.huffman;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;

import java.io.IOException;
import java.io.InputStream;

public class HuffmanFileHeaderBuilder extends HuffmanFileHeader {
    public HuffmanFileHeaderBuilder(int[] histogram) {
        this.histogram = histogram;
    }

    public HuffmanFileHeaderBuilder() {
    }

    public int[] computeHistogram(InputStream inputStream) throws IOException {
        histogram = new int[256];
        int byteIn = inputStream.read();
        while (byteIn != -1) {
            histogram[byteIn]++;
            byteIn = inputStream.read();
        }
        return histogram;
    }

    public CounterSize[] computeCounterSizes() {
        if (histogram == null) {
            throw new RuntimeException("Histogram not initialized");
        }
        counterSizes = new CounterSize[256];
        for (int i = 0; i < histogram.length; i++) {
            counterSizes[i] =
                    histogram[i] > 0 ?
                            histogram[i] < 256 ? CounterSize.SE_REPREZINTA_PE_1_OCTET
                                    : histogram[i] < 65536 ? CounterSize.SE_REPREZINTA_PE_2_OCTETI
                                    : CounterSize.SE_REPREZINTA_PE_4_OCTETI
                            : CounterSize.NU_SE_REPREZINTA;
        }
        return counterSizes;
    }

    public void writeHeader(BitWriter bitWriter) throws IOException {
        if (counterSizes == null) {
            throw new RuntimeException("Counter sizes not computed");
        }
        for (int i = 0; i < 256; i++) {
            bitWriter.writeNBitValue(counterSizes[i].ordinal(), 2);
        }
        for (int i = 0; i < 256; i++) {
            if (counterSizes[i] != CounterSize.NU_SE_REPREZINTA) {
                bitWriter.writeNBitValue(histogram[i], counterSizes[i].getNrBytes() * 8);
            }
        }
    }
}
