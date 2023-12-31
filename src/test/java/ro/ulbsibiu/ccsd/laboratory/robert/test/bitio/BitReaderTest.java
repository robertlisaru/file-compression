package ro.ulbsibiu.ccsd.laboratory.robert.test.bitio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
            assertEquals(1, bitReader.readBit());
            assertEquals(0, bitReader.readBit());
            assertEquals(0, bitReader.readBit());
            assertEquals(0, bitReader.readBit());
            assertEquals(0, bitReader.readBit());
            assertEquals(0, bitReader.readBit());
            assertEquals(0, bitReader.readBit());
            assertEquals(0, bitReader.readBit());
            assertEquals(0, bitReader.readBit());
            assertEquals(1, bitReader.readBit());
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
