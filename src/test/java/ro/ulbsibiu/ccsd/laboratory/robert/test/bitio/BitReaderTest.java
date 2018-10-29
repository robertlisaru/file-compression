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
    void init() {
        bytes = new byte[]{0b00000001, 0b00000010, 0b00000011, 0b00000100, 0b00000101};
        inputStream = new ByteArrayInputStream(bytes);
        bitReader = new BitReader(inputStream);
    }

    @Test
    void nextBit() {
        try {
            assertTrue(bitReader.readBitAsBoolean());
            assertFalse(bitReader.readBitAsBoolean());
            assertFalse(bitReader.readBitAsBoolean());
            assertFalse(bitReader.readBitAsBoolean());
            assertFalse(bitReader.readBitAsBoolean());
            assertFalse(bitReader.readBitAsBoolean());
            assertFalse(bitReader.readBitAsBoolean());
            assertFalse(bitReader.readBitAsBoolean());

            assertFalse(bitReader.readBitAsBoolean());
            assertTrue(bitReader.readBitAsBoolean());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readNBits() {
        boolean[] bits;
        try {
            bits = bitReader.readNBitsAsBooleanArray(12);
            assertEquals(12, bits.length);
            for (int i = 0; i < 12; i++) {
                assertEquals((bytes[i / 8] & (1 << i % 8)) == (1 << i % 8), bits[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readNBitValue() {
        try {
            assertEquals((long) 0b1000000001, bitReader.readNBitValue(10));
            assertEquals((long) 0b000000, bitReader.readNBitValue(6));
            assertEquals((long) 3, bitReader.readNBitValue(2));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
