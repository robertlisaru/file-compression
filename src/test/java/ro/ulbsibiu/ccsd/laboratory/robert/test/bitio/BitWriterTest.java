package ro.ulbsibiu.ccsd.laboratory.robert.test.bitio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BitWriterTest {
    ByteArrayOutputStream outputStream;
    BitWriter bitWriter;

    @BeforeEach
    void init() {
        outputStream = new ByteArrayOutputStream();
        bitWriter = new BitWriter(outputStream);
    }

    @Test
    void writeBitAsIntAndFlush() {
        int[] bits = new int[]{1, 1, 0, 0, 1, 1, 0};

        try {
            for (int i = 0; i < bits.length; i++) {
                bitWriter.writeBit(bits[i]);
            }
            bitWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] bytes = outputStream.toByteArray();
        for (int i = 0; i < bits.length; i++) {
            assertEquals(((bytes[i / 8] & (1 << i % 8)) == (1 << i % 8)) ? 1 : 0, bits[i]);
        }
    }

    @Test
    void writeBitAsBooleanAndFlush() {
        boolean[] bits = new boolean[]{true, true, false, false, true, true, false};
        try {
            for (int i = 0; i < bits.length; i++) {
                bitWriter.writeBit(bits[i]);
            }
            bitWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = outputStream.toByteArray();
        for (int i = 0; i < bits.length; i++) {
            assertEquals((bytes[i / 8] & (1 << i % 8)) == (1 << i % 8), bits[i]);
        }
    }

    @Test
    void writeNBitsAndFlush() {
        boolean[] bits = new boolean[]{true, true, false, false, true, true, false, true, false, false, false};
        try {
            bitWriter.writeNBits(bits.length, bits);
            bitWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = outputStream.toByteArray();
        for (int i = 0; i < bits.length; i++) {
            assertEquals((bytes[i / 8] & (1 << i % 8)) == (1 << i % 8), bits[i]);
        }
    }

    @Test
    void writeNBitValueAndFlush() {
        long value = 0b111011001001;
        try {
            bitWriter.writeNBitValue(value, 12);
            bitWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] writtenBytes = outputStream.toByteArray();
        assertEquals(0b11001001, writtenBytes[0] & 0xFF);
        assertEquals(0b1110, writtenBytes[1] & 0xFF);
    }
}
