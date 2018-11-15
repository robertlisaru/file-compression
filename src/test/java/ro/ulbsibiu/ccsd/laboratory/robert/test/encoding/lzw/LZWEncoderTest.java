package ro.ulbsibiu.ccsd.laboratory.robert.test.encoding.lzw;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.lzw.LZWEncoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LZWEncoderTest {
    @Test
    void encode() throws IOException {
        final byte[] bytes = new String("ABCDABCDABDCABCDABDDCD").getBytes(StandardCharsets.US_ASCII);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LZWEncoder encoder = new LZWEncoder(9, inputStream, outputStream);
        encoder.encode();
        inputStream.close();
        byte[] encodedBytes = outputStream.toByteArray();
        outputStream.close();
        assertEquals(17, encodedBytes.length);

        long[] expectedIndexes = new long[]{'A', 'B', 'C', 'D', 256, 258, 256, 'D', 'C', 260, 259, 'B', 'D', 263, 'D'};
        BitReader bitReader = new BitReader(new ByteArrayInputStream(encodedBytes));
        for (int i = 0; i < expectedIndexes.length; i++) {
            long index = bitReader.readNBitValue(9);
            assertEquals(expectedIndexes[i], index);
        }
    }
}
