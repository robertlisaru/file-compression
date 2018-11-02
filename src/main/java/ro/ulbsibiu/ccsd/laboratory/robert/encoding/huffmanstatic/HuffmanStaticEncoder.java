package ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class HuffmanStaticEncoder {
    private final HuffmanStaticFileHeaderBuilder fileHeaderBuilder;
    private Symbol[] dictionary;
    private List<SymbolCounter> symbolCounters;

    public HuffmanStaticEncoder() {
        fileHeaderBuilder = new HuffmanStaticFileHeaderBuilder();
    }

    public void computeHeader(InputStream inputStream) throws IOException {
        fileHeaderBuilder.computeHistogram(inputStream);
        fileHeaderBuilder.computeCounterCodes();
    }

    public void computeDictionary() {
        symbolCounters = fileHeaderBuilder.trimAndSortHistogram();
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
