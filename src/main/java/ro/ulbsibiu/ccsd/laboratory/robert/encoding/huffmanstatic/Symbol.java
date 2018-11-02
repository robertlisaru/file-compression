package ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic;

import java.security.InvalidParameterException;

public class Symbol {
    int code;
    int sizeInBits;

    public Symbol() {
        code = 0;
        sizeInBits = 0;
    }

    public Symbol(int code, int sizeInBits) {
        this.code = code;
        this.sizeInBits = sizeInBits;
    }

    public static Symbol[] newSymbolArray(int n) {
        Symbol[] symbols = new Symbol[n];
        for (int i = 0; i < n; i++) {
            symbols[i] = new Symbol();
        }
        return symbols;
    }

    public int getCode() {
        return code;
    }

    public int getSizeInBits() {
        return sizeInBits;
    }

    public void addBit(int bit) {
        if (bit != 0 && bit != 1) {
            throw new InvalidParameterException("a bit can only be 0 or 1");
        }
        code |= (bit << sizeInBits++);
    }

    @Override
    public boolean equals(Object obj) {
        Symbol otherSymbol = (Symbol) obj;
        return code == otherSymbol.code && sizeInBits == otherSymbol.sizeInBits;
    }
}