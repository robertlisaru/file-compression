package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lz77;

public class Token {
    private long length;
    private long offset;
    private long symbol;

    public Token(long length, long offset, long symbol) {
        this.length = length;
        this.offset = offset;
        this.symbol = symbol;
    }

    public Token() {
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {

        this.length = length;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getSymbol() {
        return symbol;
    }

    public void setSymbol(long newSymbol) {
        this.symbol = newSymbol;
    }

    @Override

    public boolean equals(Object obj) {
        Token otherToken = (Token) obj;
        if (otherToken.getLength() != length
                || otherToken.getOffset() != offset
                || otherToken.getSymbol() != symbol) {
            return false;
        }
        return true;
    }
}
