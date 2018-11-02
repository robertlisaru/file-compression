package ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HuffmanStaticFileHeader {
    protected int[] histogram;
    protected CounterCode[] counterCodes;

    public List<SymbolCounter> trimAndSortHistogram() {
        if (histogram == null) {
            throw new RuntimeException("Histogram not initialized");
        }
        List<SymbolCounter> trimmedHistogram;
        trimmedHistogram = new LinkedList<>();
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] > 0) {
                trimmedHistogram.add(new SymbolCounter(i, histogram[i]));
            }
        }
        Collections.sort(trimmedHistogram);
        return trimmedHistogram;
    }

    public enum CounterCode {
        NU_SE_REPREZINTA(0),
        SE_REPREZINTA_PE_1_OCTET(1),
        SE_REPREZINTA_PE_2_OCTETI(2),
        SE_REPREZINTA_PE_4_OCTETI(4);

        private int nrBytes;

        CounterCode(int nrBytes) {
            this.nrBytes = nrBytes;
        }

        public int getNrBytes() {
            return nrBytes;
        }
    }
}
