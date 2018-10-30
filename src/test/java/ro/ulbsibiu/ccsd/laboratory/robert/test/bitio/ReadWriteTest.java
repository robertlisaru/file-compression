package ro.ulbsibiu.ccsd.laboratory.robert.test.bitio;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReadWriteTest {
    BitReader bitReader;
    BitWriter bitWriter;
    ByteArrayOutputStream outputStream;
    InputStream inputStream;

    @Test
    void readAfterWrite() throws IOException {
        outputStream = new ByteArrayOutputStream();
        bitWriter = new BitWriter(outputStream);
        bitWriter.writeNBitValue(10, 6);
        bitWriter.writeBit(1);
        bitWriter.writeNBitValue(11, 6);
        bitWriter.flush();

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        bitReader = new BitReader(inputStream);
        assertEquals(10, bitReader.readNBitValue(6));
        assertEquals(1, bitReader.readBit());
        assertEquals(11, bitReader.readNBitValue(6));
    }
}
