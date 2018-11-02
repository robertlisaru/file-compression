package ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;

import java.io.IOException;
import java.io.InputStream;

public class HuffmanStaticFileHeaderBuilder extends HuffmanStaticFileHeader {
    public HuffmanStaticFileHeaderBuilder(int[] histogram) {
        this.histogram = histogram;
    }

    public HuffmanStaticFileHeaderBuilder() {
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

    public CounterCode[] computeCounterCodes() {
        if (histogram == null) {
            throw new RuntimeException("Histogram not initialized");
        }
        counterCodes = new CounterCode[256];
        for (int i = 0; i < histogram.length; i++) {
            counterCodes[i] =
                    histogram[i] > 0 ?
                            histogram[i] < 256 ? CounterCode.SE_REPREZINTA_PE_1_OCTET
                                    : histogram[i] < 65536 ? CounterCode.SE_REPREZINTA_PE_2_OCTETI
                                    : CounterCode.SE_REPREZINTA_PE_4_OCTETI
                            : CounterCode.NU_SE_REPREZINTA;
        }
        return counterCodes;
    }

    public void writeHeader(BitWriter bitWriter) throws IOException {
        if (counterCodes == null) {
            throw new RuntimeException("Counter sizes not computed");
        }
        for (int i = 0; i < 256; i++) {
            bitWriter.writeNBitValue(counterCodes[i].ordinal(), 2);
        }
        for (int i = 0; i < 256; i++) {
            if (counterCodes[i] != CounterCode.NU_SE_REPREZINTA) {
                bitWriter.writeNBitValue(histogram[i], counterCodes[i].getNrBytes() * 8);
            }
        }
    }
}
