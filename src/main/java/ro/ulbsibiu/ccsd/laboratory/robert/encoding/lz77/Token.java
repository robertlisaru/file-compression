package ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77;

public class Token {
    private int length;
    private int offset;
    private int newSymbol;

    public Token(int length, int offset, int newSymbol) {
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

    public int getNewSymbol() {
        return newSymbol;
    }

    public void setNewSymbol(int newSymbol) {
        this.newSymbol = newSymbol;
    }
}
