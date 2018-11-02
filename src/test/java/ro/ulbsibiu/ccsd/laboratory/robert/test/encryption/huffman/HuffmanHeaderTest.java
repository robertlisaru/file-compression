package ro.ulbsibiu.ccsd.laboratory.robert.test.encryption.huffman;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;
import ro.ulbsibiu.ccsd.laboratory.robert.encryption.huffman.HuffmanFileHeaderBuilder;
import ro.ulbsibiu.ccsd.laboratory.robert.encryption.huffman.HuffmanFileHeaderReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuffmanHeaderTest {
    HuffmanFileHeaderBuilder fileHeaderBuilder;
    HuffmanFileHeaderReader fileHeaderReader;

    @Test
    void computeWriteThenReadHeader() throws IOException {
        int[] histogram = new int[256]; //{65536, 65535, 256, 255, 1, 0};
        histogram['A'] = 65536;
        histogram['D'] = 65535;
        histogram['F'] = 256;
        histogram['I'] = 255;
        histogram['K'] = 1;
        HuffmanFileHeaderBuilder.CounterSize[] expectedCounterSizes = new HuffmanFileHeaderBuilder.CounterSize[256];
        Arrays.fill(expectedCounterSizes, HuffmanFileHeaderBuilder.CounterSize.NU_SE_REPREZINTA);
        expectedCounterSizes['A'] = HuffmanFileHeaderBuilder.CounterSize.SE_REPREZINTA_PE_4_OCTETI;
        expectedCounterSizes['D'] = HuffmanFileHeaderBuilder.CounterSize.SE_REPREZINTA_PE_2_OCTETI;
        expectedCounterSizes['F'] = HuffmanFileHeaderBuilder.CounterSize.SE_REPREZINTA_PE_2_OCTETI;
        expectedCounterSizes['I'] = HuffmanFileHeaderBuilder.CounterSize.SE_REPREZINTA_PE_1_OCTET;
        expectedCounterSizes['K'] = HuffmanFileHeaderBuilder.CounterSize.SE_REPREZINTA_PE_1_OCTET;
        expectedCounterSizes['B'] = HuffmanFileHeaderBuilder.CounterSize.NU_SE_REPREZINTA;
        expectedCounterSizes['C'] = HuffmanFileHeaderBuilder.CounterSize.NU_SE_REPREZINTA;
        expectedCounterSizes['Z'] = HuffmanFileHeaderBuilder.CounterSize.NU_SE_REPREZINTA;

        fileHeaderBuilder = new HuffmanFileHeaderBuilder(histogram);
        HuffmanFileHeaderBuilder.CounterSize[] actualCounterSizes = fileHeaderBuilder.computeCounterSizes();
        for (int i = 0; i < 256; i++) {
            assertEquals(expectedCounterSizes[i], actualCounterSizes[i]);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BitWriter bitWriter = new BitWriter(byteArrayOutputStream);
        fileHeaderBuilder.writeHeader(bitWriter);
        bitWriter.flush();


        fileHeaderReader = new HuffmanFileHeaderReader();
        BitReader bitReader = new BitReader(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        actualCounterSizes = fileHeaderReader.readCounterSizes(bitReader);
        for (int i = 0; i < 256; i++) {
            assertEquals(expectedCounterSizes[i], actualCounterSizes[i]);
        }
        int[] actualHistogram = fileHeaderReader.readHistogram(bitReader);
        for (int i = 0; i < 256; i++) {
            assertEquals(histogram[i], actualHistogram[i]);
        }

    }

    @Test
    void computeHistogram() throws IOException {
        byte[] bytes = new byte[]{'A', 'B', 'C', 'D', 'E', 'C', 'D', 'E', 'C', 'D'};
        int[] expectedHistogram = new int[256];
        expectedHistogram['A'] = 1;
        expectedHistogram['B'] = 1;
        expectedHistogram['C'] = 3;
        expectedHistogram['D'] = 3;
        expectedHistogram['E'] = 2;
        fileHeaderBuilder = new HuffmanFileHeaderBuilder();
        int[] actualHistogram = fileHeaderBuilder.computeHistogram(new ByteArrayInputStream(bytes));
        for (int i = 0; i < 256; i++) {
            assertEquals(expectedHistogram[i], actualHistogram[i]);
        }
    }

    @Test
    void buildAndSortTrimmedHistogram() {
        int[] histogram = new int[256];
        histogram['A'] = 1;
        histogram['B'] = 1;
        histogram['C'] = 3;
        histogram['D'] = 3;
        histogram['E'] = 2;
        fileHeaderBuilder = new HuffmanFileHeaderBuilder(histogram);
        List<HuffmanFileHeaderBuilder.ByteCounter> trimmedHistogram = fileHeaderBuilder.trimAndSortHistogram();
        assertEquals(5, trimmedHistogram.size());
        assertEquals(new HuffmanFileHeaderBuilder.ByteCounter('A', 1), trimmedHistogram.get(0));
        assertEquals(new HuffmanFileHeaderBuilder.ByteCounter('B', 1), trimmedHistogram.get(1));
        assertEquals(new HuffmanFileHeaderBuilder.ByteCounter('E', 2), trimmedHistogram.get(2));
        assertEquals(new HuffmanFileHeaderBuilder.ByteCounter('C', 3), trimmedHistogram.get(3));
        assertEquals(new HuffmanFileHeaderBuilder.ByteCounter('D', 3), trimmedHistogram.get(4));
    }
}
