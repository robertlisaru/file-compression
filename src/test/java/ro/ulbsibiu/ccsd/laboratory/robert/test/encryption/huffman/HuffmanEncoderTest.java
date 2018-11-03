package ro.ulbsibiu.ccsd.laboratory.robert.test.encryption.huffman;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.HuffmanStaticEncoder;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.Symbol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuffmanEncoderTest {
    @Test
    void computeDictionaryEncodeWriteFlush() throws IOException, NoSuchFieldException, IllegalAccessException {
        byte[] inputBytes = new byte[]{'A', 'B', 'C', 'D', 'E', 'C', 'D', 'E', 'C', 'D'};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HuffmanStaticEncoder huffmanEncoder = new HuffmanStaticEncoder(new BitWriter(outputStream));

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

        inputStream = new ByteArrayInputStream(inputBytes);
        huffmanEncoder.encodeAndWrite(inputStream, outputStream);
        huffmanEncoder.flush();

        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(0b10011010, 0xFF & outputBytes[0]);
        assertEquals(0b11100011, 0xFF & outputBytes[1]);
        assertEquals(0b00111000, 0xFF & outputBytes[2]);
    }
}
