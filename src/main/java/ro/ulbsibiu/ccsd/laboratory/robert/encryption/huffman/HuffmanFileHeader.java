package ro.ulbsibiu.ccsd.laboratory.robert.encryption.huffman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HuffmanFileHeader {
    protected int[] histogram;
    protected CounterSize[] counterSizes;
    protected List<ByteCounter> trimmedHistogram;

    public List trimAndSortHistogram() {
        if (histogram == null) {
            throw new RuntimeException("Histogram not initialized");
        }
        trimmedHistogram = new ArrayList<>();
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] > 0) {
                trimmedHistogram.add(new ByteCounter(i, histogram[i]));
            }
        }
        Collections.sort(trimmedHistogram);
        return trimmedHistogram;
    }

    public enum CounterSize {
        NU_SE_REPREZINTA(0),
        SE_REPREZINTA_PE_1_OCTET(1),
        SE_REPREZINTA_PE_2_OCTETI(2),
        SE_REPREZINTA_PE_4_OCTETI(4);

        private int nrBytes;

        CounterSize(int nrBytes) {
            this.nrBytes = nrBytes;
        }

        public int getNrBytes() {
            return nrBytes;
        }
    }

    public static class ByteCounter implements Comparable {
        private int value;
        private int count;

        public ByteCounter(int value, int count) {
            this.value = value;
            this.count = count;
        }

        public int getValue() {
            return value;
        }

        public int getCount() {
            return count;
        }

        @Override
        public int compareTo(Object o) {
            return count - ((ByteCounter) o).count;
        }

        @Override
        public boolean equals(Object obj) {
            return ((ByteCounter) obj).count == count
                    && ((ByteCounter) obj).value == value;
        }
    }
}
