package ro.ulbsibiu.ccsd.laboratory.robert.test.encoding.lz77;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77.CircularArrayList;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77.SlidingWindow;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SlidingWindowTest {
    @Test
    void slidingWindowTest() throws IOException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        byte[] inputBytes = new byte[]{1, 2, 3, 4, 5, 6, 7};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        SlidingWindow slidingWindow = new SlidingWindow(3, 3, inputStream);
        //region Reflection
        Field field = slidingWindow.getClass().getDeclaredField("circularList");
        field.setAccessible(true);
        CircularArrayList<Integer> reflectedCircularList = (CircularArrayList<Integer>) field.get(slidingWindow);
        Method searchBufferIsFull = slidingWindow.getClass().getDeclaredMethod("searchBufferIsFull");
        searchBufferIsFull.setAccessible(true);
        Method lookAheadBufferIsFull = slidingWindow.getClass().getDeclaredMethod("lookAheadBufferIsFull");
        lookAheadBufferIsFull.setAccessible(true);
        //endregion

        assertEquals(14, reflectedCircularList.capacity());
        assertEquals(6, reflectedCircularList.size());
        assertEquals(false, searchBufferIsFull.invoke(slidingWindow));
        assertTrue((boolean) lookAheadBufferIsFull.invoke(slidingWindow));
        for (int i = 0; i < 6; i++) {
            assertEquals((int) inputBytes[i], (int) reflectedCircularList.get(i));
        }
        slidingWindow.slide(1);
        assertEquals(7, reflectedCircularList.size());

    }
}
