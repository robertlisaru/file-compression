package ro.ulbsibiu.ccsd.laboratory.robert.test.bitio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.WriteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WriteBufferTest {
    WriteBuffer writeBuffer;

    @BeforeEach
    void init() {
        writeBuffer = new WriteBuffer();
    }

    @Test
    void bufferLenghtShouldIncreaseAfterWrite() {
        writeBuffer.putBit(1);
        assertEquals(1, writeBuffer.length());
        writeBuffer.putBit(1);
        assertEquals(2, writeBuffer.length());
    }

    @Test
    void put8BitsThenGetByteAndClear() {
        byte initialByte = 42;
        for (int i = 0; i < 8; i++) {
            writeBuffer.putBit(initialByte >> i & 1);
        }
        assertEquals(initialByte, writeBuffer.getByteAndClear());
        assertEquals(0, writeBuffer.length());
    }

    @Test
    void isFullWhenTrue() {
        byte initialByte = 42;
        for (int i = 0; i < 8; i++) {
            writeBuffer.putBit(initialByte >> i & 1);
        }
        assertTrue(writeBuffer.isFull());
    }

    @Test
    void isFullWhenFalse() {
        writeBuffer.putBit(0);
        assertFalse(writeBuffer.isFull());
    }
}
