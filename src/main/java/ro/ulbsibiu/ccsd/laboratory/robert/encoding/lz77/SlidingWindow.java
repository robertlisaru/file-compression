package ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77;

import java.io.IOException;
import java.io.InputStream;

public class SlidingWindow {
    private CircularArrayList<Integer> circularList;
    private InputStream inputStream;
    private int searchSize = 0;
    private int lookAheadSize = 0;
    private int searchCapacity;
    private int lookAheadCapacity;
    private boolean streamEnded = false;

    public SlidingWindow(int nrBitsForOffset, int nrBitsForLength, InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        searchCapacity = (1 << nrBitsForOffset);
        lookAheadCapacity = (1 << nrBitsForLength) - 2;
        circularList = new CircularArrayList<>(lookAheadCapacity + searchCapacity);
        int readByte;
        do {
            readByte = inputStream.read();
            circularList.add(circularList.size(), readByte);
            lookAheadSize++;
        } while (readByte != -1 && lookAheadSize < lookAheadCapacity);
        if (readByte == -1) {
            streamEnded = true;
        }
    }

    private boolean searchBufferIsFull() {
        return searchSize == searchCapacity;
    }

    private boolean lookAheadBufferIsFull() {
        return lookAheadSize == lookAheadCapacity;
    }

    public void slide(int slideLength) throws IOException {
        for (int i = 0; i < slideLength && lookAheadSize > 0; i++) {
            if (streamEnded == false) {
                int readByte = inputStream.read();
                if (searchBufferIsFull() && lookAheadBufferIsFull()) {
                    circularList.remove(0);
                }
                if (!searchBufferIsFull()) {
                    searchSize++;
                }
                circularList.add(lookAheadSize, readByte);
                if (!lookAheadBufferIsFull()) {
                    lookAheadSize++;
                }
                if (readByte == -1) {
                    streamEnded = true;
                }
            } else { //stream ended
                circularList.remove(0);
                lookAheadSize--;
            }
        }
    }
}
