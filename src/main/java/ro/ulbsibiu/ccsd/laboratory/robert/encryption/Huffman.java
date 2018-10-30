package ro.ulbsibiu.ccsd.laboratory.robert.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Huffman implements FileCompressionAlgorithm {
    private long[] histogram = new long[256];

    @Override
    public void compress(File inputFile, File outputFile) throws IOException {
        //region Compute Histogram
        InputStream fileInputStream = new FileInputStream(inputFile);
        int byteIn = fileInputStream.read();
        while (byteIn != -1) {
            histogram[byteIn]++;
            byteIn = fileInputStream.read();
        }
        //endregion

        //region Compute Codes

        //endregion

    }

    private class ByteCounter implements Comparable {
        int count = 0;
        byte value;

        public ByteCounter(byte value) {
            this.value = value;
        }


        @Override
        public int compareTo(Object o) {
            return count - ((ByteCounter) o).count;
        }
    }
}
