package ro.ulbsibiu.ccsd.laboratory.robert.test.encryption.huffman;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.SymbolCounter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SymbolCounterTest {
    @Test
    void mergeSymbolCounters() {
        SymbolCounter symbolCounter1 = new SymbolCounter(Character.getNumericValue('A'), 5);
        SymbolCounter symbolCounter2 = new SymbolCounter(Character.getNumericValue('B'), 6);
        SymbolCounter symbolCounter3 = new SymbolCounter(Character.getNumericValue('C'), 7);
        SymbolCounter symbolCounter4 = new SymbolCounter(Character.getNumericValue('D'), 8);

        SymbolCounter symbolCounter5 = SymbolCounter.merge(symbolCounter1, symbolCounter2);
        assertEquals(11, symbolCounter5.getCount());
        assertEquals(2, symbolCounter5.getSymbols().size());
        assertTrue(symbolCounter5.getSymbols().contains(Character.getNumericValue('A')));
        assertTrue(symbolCounter5.getSymbols().contains(Character.getNumericValue('B')));

        SymbolCounter symbolCounter6 = SymbolCounter.merge(symbolCounter3, symbolCounter4);
        assertEquals(15, symbolCounter6.getCount());
        assertEquals(2, symbolCounter6.getSymbols().size());
        assertTrue(symbolCounter6.getSymbols().contains(Character.getNumericValue('C')));
        assertTrue(symbolCounter6.getSymbols().contains(Character.getNumericValue('D')));

        SymbolCounter symbolCounter7 =SymbolCounter.merge(symbolCounter5, symbolCounter6);
        assertEquals(26, symbolCounter7.getCount());
        assertEquals(4, symbolCounter7.getSymbols().size());
        assertTrue(symbolCounter7.getSymbols().contains(Character.getNumericValue('A')));
        assertTrue(symbolCounter7.getSymbols().contains(Character.getNumericValue('B')));
        assertTrue(symbolCounter7.getSymbols().contains(Character.getNumericValue('C')));
        assertTrue(symbolCounter7.getSymbols().contains(Character.getNumericValue('D')));
    }
}
