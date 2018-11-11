package ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77;

public class Token {
    private int length;
    private int offset;
    private byte newSymbol;

    public Token(int length, int offset, byte newSymbol) {
        this.length = length;
        this.offset = offset;
        this.newSymbol = newSymbol;
    }

    public Token() {
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {

        this.length = length;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public byte getNewSymbol() {
        return newSymbol;
    }

    public void setNewSymbol(byte newSymbol) {
        this.newSymbol = newSymbol;
    }
}
