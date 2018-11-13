package ro.ulbsibiu.ccsd.laboratory.robert.test.encoding.lz77;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77.SlidingWindow;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77.Token;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SlidingWindowTest {
    @Test
    void slidingWindowTest() throws IOException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        byte[] inputBytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        SlidingWindow slidingWindow = new SlidingWindow(3, 3, inputStream);

        assertEquals(16, slidingWindow.capacity());
        assertEquals(8, slidingWindow.size());
        assertEquals(8, slidingWindow.lookAheadSize());
        assertFalse(slidingWindow.searchBufferIsFull());
        assertTrue(slidingWindow.lookAheadBufferIsFull());
        for (int i = 0; i < slidingWindow.lookAheadSize(); i++) {
            assertEquals((int) inputBytes[i], slidingWindow.get(i));
        }
        assertFalse(slidingWindow.streamEnded());
        assertEquals(0, slidingWindow.searchSize());
        slidingWindow.slide(1);
        assertEquals(9, slidingWindow.size());
        assertEquals(1, slidingWindow.searchSize());
        assertEquals(8, slidingWindow.lookAheadSize());
        assertEquals(8, slidingWindow.get(slidingWindow.size() - 1));
        slidingWindow.slide(1);
        assertEquals(2, slidingWindow.searchSize());
        assertEquals(8, slidingWindow.lookAheadSize());
        assertEquals(10, slidingWindow.size());
        assertFalse(slidingWindow.streamEnded());
        assertEquals(2, slidingWindow.lookAheadStartIndex());
        assertEquals(9, slidingWindow.get(slidingWindow.size() - 1));
        slidingWindow.slide(4);
        assertEquals(14, slidingWindow.size());
        assertEquals(6, slidingWindow.searchSize());
        assertEquals(6, slidingWindow.lookAheadStartIndex());
        assertEquals(8, slidingWindow.lookAheadSize());
        assertEquals(0, slidingWindow.get(0));
        assertEquals(13, slidingWindow.get(slidingWindow.size() - 1));
        slidingWindow.slide(2);
        assertEquals(16, slidingWindow.size());
        assertEquals(15, slidingWindow.get(slidingWindow.size() - 1));
        assertEquals(14, slidingWindow.get(slidingWindow.size() - 2));
        assertEquals(8, slidingWindow.searchSize());
        assertEquals(8, slidingWindow.lookAheadStartIndex());
        assertEquals(8, slidingWindow.lookAheadSize());
        assertEquals(0, slidingWindow.get(0));
        assertFalse(slidingWindow.streamEnded());
        slidingWindow.slide(5);
        assertTrue(slidingWindow.streamEnded());
        assertEquals(11, slidingWindow.size());
        assertEquals(15, slidingWindow.get(slidingWindow.size() - 1));
        assertEquals(8, slidingWindow.searchSize());
        assertEquals(8, slidingWindow.lookAheadStartIndex());
        assertEquals(3, slidingWindow.lookAheadSize());
        assertEquals(5, slidingWindow.get(0));
    }

    @Test
    void nextToken() throws IOException {
        byte[] bytes = new String("abracadabra").getBytes(StandardCharsets.US_ASCII);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        SlidingWindow slidingWindow = new SlidingWindow(3, 3, inputStream);

        Token token = slidingWindow.nextToken();
        assertEquals(1, slidingWindow.searchSize());
        assertEquals(new Token(0, 0, 'a'), token);

        token = slidingWindow.nextToken();
        assertEquals(2, slidingWindow.searchSize());
        assertEquals(new Token(0, 0, 'b'), token);

        token = slidingWindow.nextToken();
        assertEquals(3, slidingWindow.searchSize());
        assertEquals(new Token(0, 0, 'r'), token);

        token = slidingWindow.nextToken();
        assertEquals(5, slidingWindow.searchSize());
        assertEquals(new Token(1, 2, 'c'), token);

        token = slidingWindow.nextToken();
        assertEquals(7, slidingWindow.searchSize());
        assertEquals(new Token(1, 1, 'd'), token);

        token = slidingWindow.nextToken();
        assertEquals(8, slidingWindow.searchSize());
        assertEquals(new Token(3, 6, 'a'), token);
    }
}
