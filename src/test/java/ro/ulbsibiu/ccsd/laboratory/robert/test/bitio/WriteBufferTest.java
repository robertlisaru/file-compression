package ro.ulbsibiu.ccsd.laboratory.robert.test.bitio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.FullBufferException;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.WriteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WriteBufferTest {
    WriteBuffer writeBuffer;

    @BeforeEach
    void arrange() {
        writeBuffer = new WriteBuffer();
    }

    @Test
    void writeWhenFullShouldThrowException() {
        for (int i = 0; i < 8; i++) {
            writeBuffer.putBit(false);
        }
        assertThrows(FullBufferException.class, () -> {
            writeBuffer.putBit(false);
        });
    }

    @Test
    void bufferLenghtShouldIncreaseAfterWrite() {
        writeBuffer.putBit(true);
        assertEquals(1, writeBuffer.length());
        writeBuffer.putBit(true);
        assertEquals(2, writeBuffer.length());
    }

    @Test
    void put8BitsThenGetByteAndClear() {
        byte initialByte = 42;
        for (int i = 0; i < 8; i++) {
            writeBuffer.putBit((initialByte & (1 << i)) == (1 << i));
        }
        assertEquals(initialByte, writeBuffer.getByteAndClear());
        assertEquals(0, writeBuffer.length());
    }

    @Test
    void isFullWhenTrue() {
        byte initialByte = 42;
        for (int i = 0; i < 8; i++) {
            writeBuffer.putBit((initialByte & (1 << i)) == (1 << i));
        }
        assertTrue(writeBuffer.isFull());
    }

    @Test
    void isFullWhenFalse() {
        writeBuffer.putBit(false);
        assertFalse(writeBuffer.isFull());
    }
}
