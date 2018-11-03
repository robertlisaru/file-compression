package ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic;

import java.util.Collections;
import java.util.List;

public class HuffmanStatic {
    protected final HuffmanStaticFileHeaderProcessor fileHeaderProcessor;
    protected Symbol[] dictionary;
    protected List<SymbolCounter> symbolCounters;

    protected HuffmanStatic(HuffmanStaticFileHeaderProcessor fileHeaderProcessor) {
        this.fileHeaderProcessor = fileHeaderProcessor;
    }

    public void computeDictionary() {
        symbolCounters = fileHeaderProcessor.trimAndSortHistogram();
        dictionary = Symbol.newSymbolArray(256);

        while (symbolCounters.size() > 1) {
            SymbolCounter mergedCounter = SymbolCounter.merge(symbolCounters.get(0), symbolCounters.get(1));
            for (Integer i : symbolCounters.get(0).getSymbols()) {
                dictionary[i].addBit(0);
            }
            for (Integer i : symbolCounters.get(1).getSymbols()) {
                dictionary[i].addBit(1);
            }
            symbolCounters.remove(0);
            symbolCounters.remove(0);
            symbolCounters.add(mergedCounter);
            Collections.sort(symbolCounters);
        }
    }
}
