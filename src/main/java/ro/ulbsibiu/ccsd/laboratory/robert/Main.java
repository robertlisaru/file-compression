package ro.ulbsibiu.ccsd.laboratory.robert;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main {
    public static void main(String[] args) {
        long time0 = System.currentTimeMillis();
        File inputFile = new File("file.jpg");
        File outputFile = new File("output.jpg");
        try {
            outputFile.createNewFile();
            final InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
            final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            final BitReader bitReader = new BitReader(inputStream);
            final BitWriter bitWriter = new BitWriter(outputStream);
            for (int i = 0; i < inputFile.length(); i++) {
                bitWriter.writeNBitValue(bitReader.readNBitValue(8),8);
            }
            bitWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - time0);
    }
}
