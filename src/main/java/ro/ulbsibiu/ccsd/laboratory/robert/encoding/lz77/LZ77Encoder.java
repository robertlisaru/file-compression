package ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LZ77Encoder {
    private int nrBitsForOffset;
    private int nrBitsForLength;
    private OutputStream outputStream;
    private BitWriter bitWriter;
    private SlidingWindow slidingWindow;

    public void encode(int nrBitsForOffset, int nrBitsForLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        slidingWindow = new SlidingWindow(nrBitsForOffset, nrBitsForLength, inputStream);
        bitWriter = new BitWriter(outputStream);
        bitWriter.writeNBitValue(nrBitsForLength, 3);
        bitWriter.writeNBitValue(nrBitsForOffset, 4);
        while (slidingWindow.lookAheadSize() > 0) {
            Token token = slidingWindow.nextToken();
            bitWriter.writeNBitValue(token.getLength(), nrBitsForLength);
            bitWriter.writeNBitValue(token.getOffset(), nrBitsForOffset);
            bitWriter.writeNBitValue(token.getSymbol(), 8);
        }
        bitWriter.flush();
    }

    public boolean slidingWindowHasMoreBytes() {
        return slidingWindow.lookAheadSize() > 0;
    }

    public void prepareStepByStepEncoding(int nrBitsForOffset, int nrBitsForLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        this.nrBitsForOffset = nrBitsForOffset;
        this.nrBitsForLength = nrBitsForLength;
        this.outputStream = outputStream;
        slidingWindow = new SlidingWindow(nrBitsForOffset, nrBitsForLength, inputStream);
        bitWriter = new BitWriter(outputStream);
        bitWriter.writeNBitValue(nrBitsForLength, 3);
        bitWriter.writeNBitValue(nrBitsForOffset, 4);
    }

    public Token nextToken() throws IOException {
        Token token = slidingWindow.nextToken();
        bitWriter.writeNBitValue(token.getLength(), nrBitsForLength);
        bitWriter.writeNBitValue(token.getOffset(), nrBitsForOffset);
        bitWriter.writeNBitValue(token.getSymbol(), 8);
        return token;
    }

    public void flush() throws IOException {
        bitWriter.flush();
    }
}
