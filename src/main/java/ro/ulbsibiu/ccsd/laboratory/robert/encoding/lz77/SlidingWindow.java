package ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

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
        lookAheadCapacity = (1 << nrBitsForLength);
        circularList = new CircularArrayList<>(lookAheadCapacity + searchCapacity);
        while (lookAheadSize < lookAheadCapacity) {
            int readByte = inputStream.read();
            if (readByte == -1) {
                streamEnded = true;
                break;
            } else {
                circularList.add(circularList.size(), readByte);
                lookAheadSize++;
            }
        }
    }

    public boolean searchBufferIsFull() {
        return searchSize == searchCapacity;
    }

    public boolean lookAheadBufferIsFull() {
        return lookAheadSize == lookAheadCapacity;
    }

    public int searchSize() {
        return searchSize;
    }

    public int lookAheadSize() {
        return lookAheadSize;
    }

    public boolean streamEnded() {
        return streamEnded;
    }

    public int get(int index) {
        return circularList.get(index);
    }

    public int lookAheadStartIndex() {
        return searchSize;
    }

    public int size() {
        if (circularList.size() != (searchSize + lookAheadSize)) {
            throw new IllegalStateException("Circular list's size differs from lookAheadSize + searchSize");
        }
        return circularList.size();
    }

    public int capacity() {
        if (circularList.capacity() != (searchCapacity + lookAheadCapacity)) {
            throw new IllegalStateException("Circular list's capacity differs from lookAheadCapacity + searchCapacity");
        }
        return circularList.capacity();
    }

    public void slide(long slideLength) throws IOException {
        if (lookAheadSize == 1 && streamEnded()) {
            throw new RuntimeException("Stream ended & look ahead is empty");
        }
        if (slideLength < 1) {
            throw new InvalidParameterException("Cannot slide with the length of " + slideLength + " bytes");
        }
        if (slideLength > lookAheadSize) {
            throw new IndexOutOfBoundsException("Cannot slide more than the LookAheadBuffer's size at once. " +
                    "Analyze those bytes, don't just slide over them, boy!");
        }
        if (!lookAheadBufferIsFull() && !streamEnded) {
            throw new IllegalStateException("Stream didn't end, but LAB is not full.");
        }
        for (int i = 0; i < slideLength; i++) {
            int readByte = 0;
            if (!streamEnded()) { //stream might end now
                readByte = inputStream.read();
                if (readByte == -1) {
                    streamEnded = true;
                }
            }
            if (!streamEnded()) {
                if (searchBufferIsFull()) {
                    circularList.remove(0); //remove from start
                } else {
                    searchSize++;
                }
                circularList.add(circularList.size(), readByte); //add to end
            } else { //stream ended
                if (lookAheadSize > 0) {
                    if (searchBufferIsFull()) {
                        circularList.remove(0);
                    } else {
                        searchSize++;
                    }
                    lookAheadSize--;
                } else {
                    throw new UnsupportedOperationException("Cannot slide when stream ended & LAB is empty");
                }
            }
        }
    }

    public Token nextToken() throws IOException {
        if (lookAheadSize() == 0) {
            throw new RuntimeException("Cannot build next token. Look ahead is empty.");
        }
        Token token = new Token(0, 0, get(lookAheadStartIndex()));
        if (lookAheadSize > 1) {
            for (int sbIndex = searchSize - 1; sbIndex >= 0; sbIndex--) {
                int newLength = 0;
                while (get(sbIndex + newLength) == get(lookAheadStartIndex() + newLength)
                        && newLength < lookAheadSize - 1) {
                    newLength++;
                }
                if (newLength > token.getLength()) {
                    token.setLength(newLength);
                    token.setOffset(searchSize - sbIndex - 1); //indexing SB from right to left
                    token.setSymbol(get(lookAheadStartIndex() + newLength));
                    if (newLength == lookAheadSize - 1) {
                        break; //Cease the searching, longest possible sequence was found!
                    }
                }
            }
        }
        slide(token.getLength() + 1);
        return token;
    }
}
