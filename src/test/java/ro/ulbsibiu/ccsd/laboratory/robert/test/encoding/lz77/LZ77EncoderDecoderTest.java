package ro.ulbsibiu.ccsd.laboratory.robert.test.encoding.lz77;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lz77.LZ77Decoder;
import ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lz77.LZ77Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LZ77EncoderDecoderTest {
    @Test
    void encodingTest() throws IOException {
        for (int nrBitsForOffset = 3; nrBitsForOffset <= 15; nrBitsForOffset++) {
            for (int nrBitsForLength = 2; nrBitsForLength <= 7; nrBitsForLength++) {
                byte[] originalBytes = new String("The quick brown fox jumps over the lazy dog." +
                        ";asodjfposadifhapoguhwagnpaoiuhszjfng pougha peut pusdhg.hgzous;e" +
                        "pasouhgpaoiughpsaugwjregheprgusduggjpaoihjg;lkjhseltsluytszhgpaiuhr")
                        .getBytes(StandardCharsets.US_ASCII);

                //<editor-fold desc="encode">
                ByteArrayInputStream inputStream = new ByteArrayInputStream(originalBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                LZ77Encoder encoder = new LZ77Encoder();
                encoder.encode(nrBitsForOffset, nrBitsForLength, inputStream, outputStream);
                outputStream.flush();
                byte[] encodedBytes = outputStream.toByteArray();
                outputStream.close();
                inputStream.close();
                //</editor-fold>

                //<editor-fold desc="decode">
                inputStream = new ByteArrayInputStream(encodedBytes);
                outputStream = new ByteArrayOutputStream();
                LZ77Decoder decoder = new LZ77Decoder();
                decoder.decode(inputStream, outputStream);
                outputStream.flush();
                byte[] decodedBytes = outputStream.toByteArray();
                outputStream.close();
                inputStream.close();
                //</editor-fold>

                assertEquals(originalBytes.length, decodedBytes.length);
                for (int i = 0; i < originalBytes.length; i++) {
                    assertEquals(originalBytes[i], decodedBytes[i]);
                }
            }
        }
    }
}
