package ro.ulbsibiu.ccsd.laboratory.robert;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;

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
            InputStream inputStream = new FileInputStream(inputFile);
            OutputStream outputStream = new FileOutputStream(outputFile);
            BitReader bitReader = new BitReader(inputStream);
            BitWriter bitWriter = new BitWriter(outputStream);
            for (int i = 0; i < inputFile.length() * 8; i++) {
                bitWriter.writeBit(bitReader.readBitAsInt());
            }
            bitWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - time0);
    }
}
