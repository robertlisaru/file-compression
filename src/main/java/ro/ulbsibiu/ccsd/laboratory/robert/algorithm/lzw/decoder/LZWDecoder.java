package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.decoder;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LZWDecoder {
    private BitReader bitReader;
    private OutputStream outputStream;
    private Dictionary dictionary;
    private int numBitsForIndex;

    public LZWDecoder(InputStream inputStream, OutputStream outputStream) {
        bitReader = new BitReader(inputStream);
        this.outputStream = outputStream;
    }

    public void decode() throws IOException {
        int headerCode = bitReader.readBit();
        numBitsForIndex = (int) bitReader.readNBitValue(4);
        dictionary = headerCode == 0 ? new FreezeStrategyDictionary(numBitsForIndex)
                : new EmptyStrategyDictionary(numBitsForIndex);
        int index = (int) bitReader.readNBitValue(numBitsForIndex);
        Sequence sequence = dictionary.getSequence(index);
        byte oldB = sequence.array[sequence.size - 1];
        writeReversedSequence(sequence);
        int prefix = index;
        try {
            while (true) {
                index = (int) bitReader.readNBitValue(numBitsForIndex);
                if (dictionary.hasSequence(index)) {
                    sequence = dictionary.getSequence(index);
                    oldB = sequence.array[sequence.size - 1];
                    byte b = sequence.array[sequence.size - 1];
                    writeReversedSequence(sequence);
                    if (dictionary.isFull()) {
                        if (dictionary.evacuate()) {
                            dictionary.add(prefix, b);
                        }
                    } else {
                        dictionary.add(prefix, b);
                    }
                } else {
                    if (dictionary.isFull()) {
                        if (dictionary.evacuate()) {
                            dictionary.add(prefix, oldB);
                        }
                    } else {
                        dictionary.add(prefix, oldB);
                    }
                    writeReversedSequence(dictionary.getSequence(prefix));
                    outputStream.write(oldB);
                }
                prefix = index;
            }
        } catch (UnsupportedOperationException e) {

        }
        outputStream.flush();
    }

    private void writeReversedSequence(Sequence sequence) throws IOException {
        for (int i = sequence.size - 1; i >= 0; i--) {
            outputStream.write(sequence.array[i]);
        }
    }
}
