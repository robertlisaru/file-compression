package ro.ulbsibiu.ccsd.laboratory.robert.test.encryption.huffman;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.HuffmanStaticFileHeader;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.HuffmanStaticFileHeaderBuilder;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.HuffmanStaticFileHeaderReader;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.SymbolCounter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuffmanHeaderTest {
    HuffmanStaticFileHeaderBuilder fileHeaderBuilder;
    HuffmanStaticFileHeaderReader fileHeaderReader;

    @Test
    void computeWriteThenReadHeader() throws IOException {
        int[] histogram = new int[256]; //{65536, 65535, 256, 255, 1, 0};
        histogram['A'] = 65536;
        histogram['D'] = 65535;
        histogram['F'] = 256;
        histogram['I'] = 255;
        histogram['K'] = 1;
        HuffmanStaticFileHeader.CounterCode[] expectedCounterSizes = new HuffmanStaticFileHeader.CounterCode[256];
        Arrays.fill(expectedCounterSizes, HuffmanStaticFileHeader.CounterCode.NU_SE_REPREZINTA);
        expectedCounterSizes['A'] = HuffmanStaticFileHeader.CounterCode.SE_REPREZINTA_PE_4_OCTETI;
        expectedCounterSizes['D'] = HuffmanStaticFileHeader.CounterCode.SE_REPREZINTA_PE_2_OCTETI;
        expectedCounterSizes['F'] = HuffmanStaticFileHeader.CounterCode.SE_REPREZINTA_PE_2_OCTETI;
        expectedCounterSizes['I'] = HuffmanStaticFileHeader.CounterCode.SE_REPREZINTA_PE_1_OCTET;
        expectedCounterSizes['K'] = HuffmanStaticFileHeader.CounterCode.SE_REPREZINTA_PE_1_OCTET;
        expectedCounterSizes['B'] = HuffmanStaticFileHeader.CounterCode.NU_SE_REPREZINTA;
        expectedCounterSizes['C'] = HuffmanStaticFileHeader.CounterCode.NU_SE_REPREZINTA;
        expectedCounterSizes['Z'] = HuffmanStaticFileHeader.CounterCode.NU_SE_REPREZINTA;

        fileHeaderBuilder = new HuffmanStaticFileHeaderBuilder(histogram);
        HuffmanStaticFileHeader.CounterCode[] actualCounterSizes = fileHeaderBuilder.computeCounterCodes();
        for (int i = 0; i < 256; i++) {
            assertEquals(expectedCounterSizes[i], actualCounterSizes[i]);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BitWriter bitWriter = new BitWriter(byteArrayOutputStream);
        fileHeaderBuilder.writeHeader(bitWriter);
        bitWriter.flush();


        fileHeaderReader = new HuffmanStaticFileHeaderReader();
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
        fileHeaderBuilder = new HuffmanStaticFileHeaderBuilder();
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
        fileHeaderBuilder = new HuffmanStaticFileHeaderBuilder(histogram);
        List<SymbolCounter> trimmedHistogram = fileHeaderBuilder.trimAndSortHistogram();
        assertEquals(5, trimmedHistogram.size());
        assertEquals(new SymbolCounter('A', 1), trimmedHistogram.get(0));
        assertEquals(new SymbolCounter('B', 1), trimmedHistogram.get(1));
        assertEquals(new SymbolCounter('E', 2), trimmedHistogram.get(2));
        assertEquals(new SymbolCounter('C', 3), trimmedHistogram.get(3));
        assertEquals(new SymbolCounter('D', 3), trimmedHistogram.get(4));
    }
}
