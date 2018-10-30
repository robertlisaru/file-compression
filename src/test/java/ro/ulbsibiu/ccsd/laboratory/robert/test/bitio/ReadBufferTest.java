package ro.ulbsibiu.ccsd.laboratory.robert.test.bitio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.ReadBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReadBufferTest {
    ReadBuffer buffer;

    @BeforeEach
    void init() {
        buffer = new ReadBuffer();
    }

    @Test
    void nextBitShouldReadTheUpdatedValuesAfterRefill() {
        buffer.refill((byte) 255);
        for (int i = 0; i < 8; i++) {
            assertEquals(1, buffer.nextBit());
        }
        buffer.refill((byte) 0b10101010);
        for (int i = 0; i < 4; i++) {
            assertEquals(0, buffer.nextBit());
            assertEquals(1, buffer.nextBit());
        }
    }

    @Test
    void isEmptyWhenTrue() {
        assertTrue(buffer.isEmpty());
    }

    @Test
    void isEmptyWhenFalse() {
        buffer.refill((byte) 42);
        assertFalse(buffer.isEmpty());
    }

    @Test
    void bufferLengthShouldDecreaseAfterRead() {
        buffer.refill((byte) 42);
        assertEquals(8, buffer.length());
        buffer.nextBit();
        assertEquals(7, buffer.length());
    }
}
