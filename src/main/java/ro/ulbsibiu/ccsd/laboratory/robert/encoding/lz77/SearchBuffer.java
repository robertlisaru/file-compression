package ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77;

import java.util.LinkedList;

public class SearchBuffer {
    private LinkedList<Integer> SBList = new LinkedList<>();
    private int maxSize;

    public SearchBuffer(int maxSize) {
        this.maxSize = maxSize;
    }

    public void advance(int newByte) {
        if (isFull()) {
            SBList.removeFirst();
        }
        SBList.addLast(newByte);
    }

    private boolean isFull() {
        return SBList.size() >= maxSize;
    }
}
