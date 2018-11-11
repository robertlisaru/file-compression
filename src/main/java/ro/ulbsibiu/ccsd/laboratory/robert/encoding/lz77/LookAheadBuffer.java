package ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

public class LookAheadBuffer {
    private LinkedList<Integer> LABList = new LinkedList<>();
    private InputStream inputStream;
    private int maxSize;
    private boolean streamEnded = false;

    public LookAheadBuffer(InputStream inputStream, int maxSize) throws IOException {
        this.maxSize = maxSize;
        this.inputStream = inputStream;

        int readByte = inputStream.read();
        while (!isFull() && readByte != -1) {
            LABList.add(readByte);
            readByte = inputStream.read();
        }
        if (readByte == -1) {
            streamEnded = true;
        }
    }

    public int pollNextElement() throws IOException {
        if (LABList.isEmpty()) {
            throw new RuntimeException("Cannot get next element from empty look ahead buffer.");
        }

        if (!streamEnded) {
            int readByte = inputStream.read();
            if (readByte == -1) {
                streamEnded = true;
            } else {
                LABList.addLast(readByte);
            }
        }
        return LABList.pollFirst();
    }

    public boolean hasMoreItems() {
        return !LABList.isEmpty();
    }

    private boolean isFull() {
        return LABList.size() >= maxSize;
    }
}
