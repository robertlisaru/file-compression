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
    void arrange() {
        outputStream = new ByteArrayOutputStream();
        bitWriter = new BitWriter(outputStream);
    }

    @Test
    void writeBitsAndFlush() {
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
}
