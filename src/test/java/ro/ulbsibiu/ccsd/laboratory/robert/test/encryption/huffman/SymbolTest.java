package ro.ulbsibiu.ccsd.laboratory.robert.test.encryption.huffman;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.Symbol;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SymbolTest {
    @Test
    void addBit() {
        Symbol symbol = new Symbol();
        symbol.addBit(0);
        assertEquals(1, symbol.getSizeInBits());
        assertEquals(0, symbol.getCode());

        symbol.addBit(1);
        assertEquals(2, symbol.getSizeInBits());
        assertEquals(2, symbol.getCode());
    }

    @Test
    void addingNonBitShouldThrowException() {
        Symbol symbol = new Symbol();
        assertThrows(InvalidParameterException.class, () -> {
            symbol.addBit(2);
        });
    }

    @Test
    void assertEqualSymbols() {
        Symbol symbol1 = new Symbol();
        symbol1.addBit(1);
        symbol1.addBit(1);
        symbol1.addBit(0);
        Symbol symbol2 = new Symbol();
        symbol2.addBit(1);
        symbol2.addBit(1);
        symbol2.addBit(0);
        assertEquals(symbol1, symbol2);
    }
}
