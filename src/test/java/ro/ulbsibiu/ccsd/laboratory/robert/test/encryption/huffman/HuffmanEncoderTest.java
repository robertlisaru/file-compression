package ro.ulbsibiu.ccsd.laboratory.robert.test.encryption.huffman;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.HuffmanStaticEncoder;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.Symbol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuffmanEncoderTest {
    @Test
    void computeDictionary() throws IOException, NoSuchFieldException, IllegalAccessException {
        byte[] bytes = new byte[]{'A', 'B', 'C', 'D', 'E', 'C', 'D', 'E', 'C', 'D'};
        InputStream inputStream = new ByteArrayInputStream(bytes);
        HuffmanStaticEncoder huffmanEncoder = new HuffmanStaticEncoder(null);

        huffmanEncoder.computeHeader(inputStream);
        huffmanEncoder.computeDictionary();

        Field field = huffmanEncoder.getClass().getDeclaredField("dictionary");
        field.setAccessible(true);
        Symbol[] dictionary = (Symbol[]) field.get(huffmanEncoder);

        assertEquals(0b010, dictionary['A'].getCode());
        assertEquals(3, dictionary['A'].getSizeInBits());
        assertEquals(0b011, dictionary['B'].getCode());
        assertEquals(3, dictionary['B'].getSizeInBits());
        assertEquals(0b10, dictionary['C'].getCode());
        assertEquals(2, dictionary['C'].getSizeInBits());
        assertEquals(0b11, dictionary['D'].getCode());
        assertEquals(2, dictionary['D'].getSizeInBits());
        assertEquals(0b00, dictionary['E'].getCode());
        assertEquals(2, dictionary['E'].getSizeInBits());
    }
}
