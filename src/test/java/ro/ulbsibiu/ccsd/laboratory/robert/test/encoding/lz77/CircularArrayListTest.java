package ro.ulbsibiu.ccsd.laboratory.robert.test.encoding.lz77;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lz77.CircularArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CircularArrayListTest {
    CircularArrayList<Integer> circularArrayList;

    @Test
    void fillingRemovingAdding() {
        circularArrayList = new CircularArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            circularArrayList.add(i, i);
        }
        for (int i = 0; i < 5; i++) {
            assertEquals(Integer.valueOf(i), circularArrayList.get(i));
        }

        circularArrayList.remove(0);
        for (int i = 0; i < 4; i++) {
            assertEquals(Integer.valueOf(i + 1), circularArrayList.get(i));
        }

        circularArrayList.add(4, 5);
        for (int i = 0; i < 5; i++) {
            assertEquals(Integer.valueOf((i + 1)), circularArrayList.get(i));
        }
    }

    @Test
    void addAtTheEnd() {
        circularArrayList = new CircularArrayList<>(5);
        circularArrayList.add(circularArrayList.size(), 1);
        circularArrayList.add(circularArrayList.size(), 2);
        circularArrayList.add(circularArrayList.size(), 3);
        circularArrayList.add(circularArrayList.size(), 4);
        circularArrayList.add(circularArrayList.size(), 5);
        for (int i = 0; i < 5; i++) {
            assertEquals(Integer.valueOf(i + 1), circularArrayList.get(i));
        }
    }
}
