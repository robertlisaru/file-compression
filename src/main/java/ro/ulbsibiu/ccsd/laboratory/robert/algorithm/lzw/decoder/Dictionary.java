package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.decoder;

public abstract class Dictionary {
    public static final int EMPTY_PREFIX = -1;
    public static final int NULL_INDEX = -1;
    public final Sequence sequence;
    protected DictionaryEntry[] table;
    protected int size = 256;

    public Dictionary(int nrBitsForIndex) {
        table = new DictionaryEntry[1 << nrBitsForIndex];
        sequence = new Sequence(nrBitsForIndex);
        for (int i = 0; i < 256; i++) {
            table[i] = new DictionaryEntry();
            table[i].b = (byte) i;
        }
    }

    public void add(int prefix, byte b) {
        table[size] = new DictionaryEntry();
        table[size].prefix = prefix;
        table[size].b = b;
        size++;
    }

    public boolean hasSequence(int index) {
        return index < size;
    }

    public Sequence getSequence(int index) {
        sequence.size = 0;
        do {
            sequence.array[sequence.size++] = table[index].b;
            index = table[index].prefix;
        } while (index != Dictionary.EMPTY_PREFIX);
        return sequence;
    }

    public boolean isFull() {
        return size == table.length;
    }

    protected abstract boolean evacuate();
}
