package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.encoder;

public abstract class Dictionary {
    public static final int EMPTY_PREFIX = -1;
    public static final int NULL_INDEX = -1;
    protected DictionaryEntry[] table;
    protected int size = 256;

    public Dictionary(int nrBitsForIndex) {
        table = new DictionaryEntry[1 << nrBitsForIndex];
        for (int i = 0; i < 256; i++) {
            table[i] = new DictionaryEntry();
            table[i].b = (byte) i;
        }
    }

    public boolean isFull() {
        return size == table.length;
    }

    public int search(int prefix, byte b) {
        if (prefix == EMPTY_PREFIX) {
            return b & 0xFF;
        }
        int index = table[prefix].first;
        if (index == NULL_INDEX) {
            if (isFull()) {
                if (evacuate()) {
                    addAndLinkAsFirst(prefix, b);
                }
            } else {
                addAndLinkAsFirst(prefix, b);
            }
            return NULL_INDEX;
        }

        while (b != table[index].b) { //binary search
            if (b < table[index].b) {
                if (table[index].left != NULL_INDEX) {
                    index = table[index].left;
                } else {
                    if (isFull()) {
                        if (evacuate()) {
                            addAndLinkAsLeft(index, b);
                        }
                    } else { // not full
                        addAndLinkAsLeft(index, b);
                    }
                    return NULL_INDEX;
                }
            } else {
                if (table[index].right != NULL_INDEX) {
                    index = table[index].right;
                } else {
                    if (isFull()) {
                        if (evacuate()) {
                            addAndLinkAsRight(index, b);
                        }
                    } else { //not full
                        addAndLinkAsRight(index, b);
                    }
                    return NULL_INDEX;
                }
            }
        }
        return index;
    }

    protected abstract boolean evacuate();

    private void addAndLinkAsRight(int parentIndex, byte b) {
        table[size] = new DictionaryEntry();
        table[size].b = b;
        table[parentIndex].right = size;
        size++;
    }

    private void addAndLinkAsLeft(int index, byte b) {
        table[size] = new DictionaryEntry();
        table[size].b = b;
        table[index].left = size;
        size++;
    }

    private void addAndLinkAsFirst(int parentIndex, byte b) {
        table[size] = new DictionaryEntry();
        table[size].b = b;
        table[parentIndex].first = size;
        size++;
    }

    public abstract long getHeaderCode();
}
