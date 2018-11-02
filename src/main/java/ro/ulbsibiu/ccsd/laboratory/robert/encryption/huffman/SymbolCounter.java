package ro.ulbsibiu.ccsd.laboratory.robert.encryption.huffman;

import java.util.ArrayList;
import java.util.List;

public class SymbolCounter implements Comparable {
    private List<Integer> symbols = new ArrayList<>();
    private int count;

    public SymbolCounter(int symbol, int count) {
        symbols.add(symbol);
        this.count = count;
    }

    public SymbolCounter(List<Integer> symbols, int count) {
        this.symbols = symbols;
        this.count = count;
    }

    public List<Integer> getSymbols() {
        return symbols;
    }

    public int getCount() {
        return count;
    }

    public SymbolCounter mergeWith(SymbolCounter otherSymbolCounter) {
        List<Integer> mergedSymbols = new ArrayList<>();
        for (Integer i : symbols) {
            mergedSymbols.add(i);
        }
        for (Integer i : otherSymbolCounter.symbols) {
            mergedSymbols.add(i);
        }
        int totalCount = count + otherSymbolCounter.count;
        return new SymbolCounter(mergedSymbols, totalCount);
    }

    @Override
    public int compareTo(Object o) {
        return count - ((SymbolCounter) o).count;
    }

    @Override
    public boolean equals(Object obj) {
        SymbolCounter comparedSymbolCounter = (SymbolCounter) obj;
        if (count != comparedSymbolCounter.count) {
            return false;
        }
        if (symbols.size() != comparedSymbolCounter.symbols.size()) {
            return false;
        }
        for (int i = 0; i < symbols.size(); i++) {
            if (!comparedSymbolCounter.symbols.contains(symbols.get(0))) {
                return false;
            }
        }
        return true;
    }
}