package ro.ulbsibiu.ccsd.laboratory.robert.bitio;

public class WriteBuffer {
    private byte bits;
    private int pos = 0;

    public void putBit(int bit) {
        bits |= (bit << pos++);
    }

    public boolean isFull() {
        return pos == 8;
    }

    public int length() {
        return pos;
    }

    public byte getByteAndClear() {
        byte theByte = bits;
        bits = 0;
        pos = 0;
        return theByte;
    }
}
