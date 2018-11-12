package ro.ulbsibiu.ccsd.laboratory.robert.test.encoding.lz77;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77.CircularArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CircularArrayListTest {
    CircularArrayList<Byte> circularArrayList;

    @Test
    void fillingRemovingAdding() {
        circularArrayList = new CircularArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            circularArrayList.add(i, (byte) i);
        }
        for (int i = 0; i < 5; i++) {
            assertEquals(Byte.valueOf((byte) i), circularArrayList.get(i));
        }

        circularArrayList.remove(0);
        for (int i = 0; i < 4; i++) {
            assertEquals(Byte.valueOf((byte) (i + 1)), circularArrayList.get(i));
        }

        circularArrayList.add(4, (byte) 5);
        for (int i = 0; i < 5; i++) {
            assertEquals(Byte.valueOf((byte) (i + 1)), circularArrayList.get(i));
        }
    }
}
