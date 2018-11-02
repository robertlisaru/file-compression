package ro.ulbsibiu.ccsd.laboratory.robert.encryption.huffman;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;
import ro.ulbsibiu.ccsd.laboratory.robert.encryption.FileEncodingAlgorithm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HuffmanEncoder implements FileEncodingAlgorithm {
    private final HuffmanFileHeaderBuilder fileHeader;
    private final BitReader bitReader;
    private final BitWriter bitWriter;
    private final File inputFile;
    private final File outputFile;

    public HuffmanEncoder(File inputFile, File outputFile) throws FileNotFoundException {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        bitReader = new BitReader(new BufferedInputStream(new FileInputStream(inputFile)));
        bitWriter = new BitWriter(new BufferedOutputStream(new FileOutputStream(outputFile)));
        fileHeader = new HuffmanFileHeaderBuilder();
    }

    @Override
    public void encode() throws IOException {
        fileHeader.computeHistogram(new BufferedInputStream(new FileInputStream(inputFile)));
        fileHeader.computeCounterSizes();
        fileHeader.trimAndSortHistogram();

        //region Compute Codes
        int[] asdf = fileHeader.histogram;
        //endregion

    }
}
