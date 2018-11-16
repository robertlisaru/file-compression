package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.huffmanstatic;

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

    public static SymbolCounter merge(SymbolCounter symbolCounter1, SymbolCounter symbolCounter2) {
        List<Integer> mergedSymbols = new ArrayList<>();
        for (Integer i : symbolCounter2.symbols) {
            mergedSymbols.add(i);
        }
        for (Integer i : symbolCounter1.symbols) {
            mergedSymbols.add(i);
        }
        int totalCount = symbolCounter2.count + symbolCounter1.count;
        return new SymbolCounter(mergedSymbols, totalCount);
    }

    public List<Integer> getSymbols() {
        return symbols;
    }

    public int getCount() {
        return count;
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