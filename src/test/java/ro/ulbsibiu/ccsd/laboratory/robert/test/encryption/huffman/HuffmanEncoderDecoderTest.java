package ro.ulbsibiu.ccsd.laboratory.robert.test.encryption.huffman;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.HuffmanStaticDecoder;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.HuffmanStaticEncoder;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.Symbol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuffmanEncoderDecoderTest {
    @Test
    void computeDictionaryEncodeWriteFlush() throws IOException, NoSuchFieldException, IllegalAccessException {
        byte[] inputBytes = new byte[]{'A', 'B', 'C', 'D', 'E', 'C', 'D', 'E', 'C', 'D'};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HuffmanStaticEncoder encoder = new HuffmanStaticEncoder(new BitWriter(outputStream));

        encoder.computeHeader(inputStream);
        encoder.computeDictionary();

        Field field = encoder.getClass().getSuperclass().getDeclaredField("dictionary");
        field.setAccessible(true);
        Symbol[] dictionary = (Symbol[]) field.get(encoder);

        assertEquals(0b010, dictionary['A'].getCode());
        assertEquals(3, dictionary['A'].getSizeInBits());
        assertEquals(0b110, dictionary['B'].getCode());
        assertEquals(3, dictionary['B'].getSizeInBits());
        assertEquals(0b01, dictionary['C'].getCode());
        assertEquals(2, dictionary['C'].getSizeInBits());
        assertEquals(0b11, dictionary['D'].getCode());
        assertEquals(2, dictionary['D'].getSizeInBits());
        assertEquals(0b00, dictionary['E'].getCode());
        assertEquals(2, dictionary['E'].getSizeInBits());

        inputStream = new ByteArrayInputStream(inputBytes);
        encoder.encodeAndWrite(inputStream);
        encoder.flush();

        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(0b01110010, 0xFF & outputBytes[0]);
        assertEquals(0b11010011, 0xFF & outputBytes[1]);
        assertEquals(0b00110100, 0xFF & outputBytes[2]);
    }


    void encodeDecode() throws IOException {
        byte[] inputBytes = new byte[]{'A', 'B', 'C', 'D', 'E', 'C', 'D', 'E', 'C', 'D'};
        InputStream encoderInputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream encoderOutputStream = new ByteArrayOutputStream();
        HuffmanStaticEncoder encoder = new HuffmanStaticEncoder(new BitWriter(encoderOutputStream));

        encoder.computeHeader(encoderInputStream);
        encoder.computeDictionary();
        encoder.writeHeader();
        encoderInputStream = new ByteArrayInputStream(inputBytes);
        encoder.encodeAndWrite(encoderInputStream);
        encoder.flush();

        byte[] encodedBytes = encoderOutputStream.toByteArray();
        HuffmanStaticDecoder decoder =
                new HuffmanStaticDecoder(new BitReader(new ByteArrayInputStream(encodedBytes)));
        decoder.readHeader();
        decoder.computeDictionary();
        ByteArrayOutputStream decoderOutputStream = new ByteArrayOutputStream();
        decoder.decodeAndWrite(decoderOutputStream);
        byte[] decodedBytes = decoderOutputStream.toByteArray();
        for (int i = 0; i < inputBytes.length; i++) {
            assertEquals(inputBytes[i], decodedBytes[i]);
        }
    }
}
