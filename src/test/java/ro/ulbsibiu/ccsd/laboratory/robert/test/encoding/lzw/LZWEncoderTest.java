package ro.ulbsibiu.ccsd.laboratory.robert.test.encoding.lzw;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.decoder.LZWDecoder;
import ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.encoder.LZWEncoder;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LZWEncoderTest {
    @Test
    void encode() throws IOException {
        final byte[] bytes = new String("ABCDABCDABDCABCDABDDCDCD").getBytes(StandardCharsets.US_ASCII);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LZWEncoder encoder = new LZWEncoder(1, 9, inputStream, outputStream);
        encoder.encode();
        inputStream.close();
        byte[] encodedBytes = outputStream.toByteArray();
        outputStream.close();
        assertEquals(18, encodedBytes.length);

        long[] expectedIndexes = new long[]{'A', 'B', 'C', 'D', 256, 258, 256, 'D', 'C', 260, 259, 'B', 'D', 263, 269};
        BitReader bitReader = new BitReader(new ByteArrayInputStream(encodedBytes));
        bitReader.readBit();
        bitReader.readNBitValue(4);
        for (int i = 0; i < expectedIndexes.length; i++) {
            long index = bitReader.readNBitValue(9);
            assertEquals(expectedIndexes[i], index);
        }

        inputStream = new ByteArrayInputStream(encodedBytes);
        outputStream = new ByteArrayOutputStream();
        LZWDecoder decoder = new LZWDecoder(inputStream, outputStream);
        decoder.decode();
        inputStream.close();
        byte[] decodedBytes = outputStream.toByteArray();
        outputStream.close();
        assertEquals(bytes.length, decodedBytes.length);
        for (int i = 0; i < bytes.length; i++) {
            assertEquals(bytes[i], decodedBytes[i]);
        }
    }
}
