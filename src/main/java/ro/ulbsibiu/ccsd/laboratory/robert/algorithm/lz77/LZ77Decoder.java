package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lz77;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LZ77Decoder {
    private BitReader bitReader;
    private CircularArrayList<Integer> buffer;

    public void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        bitReader = new BitReader(inputStream);
        long nrBitsForLength = bitReader.readNBitValue(3);
        long nrBitsForOffset = bitReader.readNBitValue(4);
        buffer = new CircularArrayList<>((1 << nrBitsForOffset) + 1);
        try {
            while (true) {
                long length = bitReader.readNBitValue((int) nrBitsForLength);
                long offset = bitReader.readNBitValue((int) nrBitsForOffset);
                long symbol = bitReader.readNBitValue(8);
                long sequenceStartIndex = buffer.size() - 2 - offset;
                for (int i = 0; i < length; i++) {
                    if (buffer.size() == buffer.capacity()) {
                        buffer.remove(0);
                    } else {
                        sequenceStartIndex++;
                    }
                    buffer.add(buffer.size(), buffer.get((int) sequenceStartIndex));
                    outputStream.write(buffer.get((int) sequenceStartIndex));
                }
                if (buffer.size() == buffer.capacity()) {
                    buffer.remove(0);
                }
                buffer.add(buffer.size(), (int) symbol);
                outputStream.write((int) symbol);
            }
        } catch (UnsupportedOperationException e) { //end of stream

        }
        outputStream.flush();
    }
}
