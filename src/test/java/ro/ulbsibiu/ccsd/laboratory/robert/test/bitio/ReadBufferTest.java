package ro.ulbsibiu.ccsd.laboratory.robert.test.bitio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.exception.EmptyBufferException;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.ReadBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReadBufferTest {
    ReadBuffer buffer;

    @BeforeEach
    void init() {
        buffer = new ReadBuffer();
    }

    @Test
    @DisplayName("Reading from empty buffer should throw exception")
    void readWhenEmptyShouldThrowException() {
        assertThrows(EmptyBufferException.class, () -> {
            buffer.nextBit();
        });
    }

    @Test
    void nextBitShouldReadTheUpdatedValuesAfterRefill() {
        buffer.refill((byte) 255);
        for (int i = 0; i < 8; i++) {
            assertEquals(true, buffer.nextBit());
        }
        buffer.refill((byte) 0b10101010);
        for (int i = 0; i < 4; i++) {
            assertEquals(false, buffer.nextBit());
            assertEquals(true, buffer.nextBit());
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
    void emptyingThenReadingShouldThrowException() {
        buffer.refill((byte) 42);
        assertThrows(EmptyBufferException.class, () -> {
            for (int i = 0; i < 9; i++) {
                buffer.nextBit();
            }
        });
    }

    @Test
    void remainingBitsShouldBeReturnedAndBufferEmptied() {
        buffer.refill((byte) 0b00011111);
        for (int i = 0; i < 4; i++) {
            buffer.nextBit();
        }
        boolean[] remainingBits = buffer.remainingBits();
        assertTrue(remainingBits[0]);
        assertFalse(remainingBits[1]);
        assertFalse(remainingBits[2]);
        assertFalse(remainingBits[3]);

        assertEquals(4, remainingBits.length);

        assertTrue(buffer.isEmpty());
    }

    @Test
    void bufferLengthShouldDecreaseAfterRead() {
        buffer.refill((byte) 42);
        assertEquals(8, buffer.length());
        buffer.nextBit();
        assertEquals(7, buffer.length());
    }
}
