package ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;

import java.io.IOException;
import java.io.InputStream;

public class HuffmanStaticEncoder extends HuffmanStatic {
    private final BitWriter bitWriter;

    public HuffmanStaticEncoder(BitWriter bitWriter) {
        super(new HuffmanStaticFileHeaderBuilder());
        this.bitWriter = bitWriter;
    }

    public void computeHeader(InputStream inputStream) throws IOException {
        ((HuffmanStaticFileHeaderBuilder) fileHeaderProcessor).computeHistogram(inputStream);
        ((HuffmanStaticFileHeaderBuilder) fileHeaderProcessor).computeCounterCodes();
    }

    public void writeHeader() throws IOException {
        ((HuffmanStaticFileHeaderBuilder) fileHeaderProcessor).writeHeader(bitWriter);
    }

    public void encodeAndWrite(InputStream inputStream) throws IOException {
        int readByte = inputStream.read();
        while (readByte != -1) {
            bitWriter.writeNBitValue(dictionary[readByte].getCode(), dictionary[readByte].getSizeInBits());
            readByte = inputStream.read();
        }
    }

    public void flush() throws IOException {
        bitWriter.flush();
    }
}
