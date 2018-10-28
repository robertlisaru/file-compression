package ro.ulbsibiu.ccsd.laboratory.robert.test.bitio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BitReaderTest {
    byte[] bytes;
    InputStream inputStream;
    BitReader bitReader;

    @BeforeEach
    void arrange() {
        bytes = new byte[]{1, 2, 3, 4, 5};
        inputStream = new ByteArrayInputStream(bytes);
        bitReader = new BitReader(inputStream);
    }

    @Test
    void nextBit() {
        try {
            assertTrue(bitReader.readBit());
            assertFalse(bitReader.readBit());
            assertFalse(bitReader.readBit());
            assertFalse(bitReader.readBit());
            assertFalse(bitReader.readBit());
            assertFalse(bitReader.readBit());
            assertFalse(bitReader.readBit());
            assertFalse(bitReader.readBit());

            assertFalse(bitReader.readBit());
            assertTrue(bitReader.readBit());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readNBits() {
        boolean[] bits;
        try {
            bits = bitReader.readNBits(12);
            assertEquals(12, bits.length);
            for (int i = 0; i < 12; i++) {
                assertEquals((bytes[i / 8] & (1 << i % 8)) == (1 << i % 8), bits[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
