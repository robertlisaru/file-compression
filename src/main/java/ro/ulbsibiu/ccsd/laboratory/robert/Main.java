package ro.ulbsibiu.ccsd.laboratory.robert;

import ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77.LZ77Decoder;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77.LZ77Encoder;

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
        //region Huffman Encode Decode snippet
        /*long time0 = System.currentTimeMillis();
        File encoderInputFile = new File("input");
        File encoderOutputFile = new File("encoded");
        File decoderOutputFile = new File("decoded");
        try {
            encoderOutputFile.createNewFile();
            InputStream encoderInputStream = new BufferedInputStream(new FileInputStream(encoderInputFile));
            final OutputStream encoderOutputStream =
                    new BufferedOutputStream(new FileOutputStream(encoderOutputFile));
            HuffmanStaticEncoder encoder = new HuffmanStaticEncoder(new BitWriter(encoderOutputStream));
            encoder.computeHeader(encoderInputStream);
            encoder.computeDictionary();
            encoder.writeHeader();
            encoderInputStream = new BufferedInputStream(new FileInputStream(encoderInputFile));
            encoder.encodeAndWrite(encoderInputStream);
            encoder.flush();
            encoderOutputStream.close();

            decoderOutputFile.createNewFile();
            InputStream decoderInputStream = new BufferedInputStream(new FileInputStream(encoderOutputFile));
            OutputStream decoderOutputStream = new BufferedOutputStream(new FileOutputStream(decoderOutputFile));
            HuffmanStaticDecoder decoder = new HuffmanStaticDecoder(new BitReader(decoderInputStream));
            decoder.readHeader();
            decoder.computeDictionary();
            decoder.decodeAndWrite(decoderOutputStream);
            decoderOutputStream.flush();
            decoderOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - time0);*/
        //endregion

        // region LZ77 Encode Decode snippet
        long time0 = System.currentTimeMillis();
        File encoderInputFile = new File("input");
        File encoderOutputFile = new File("encoded");
        File decoderOutputFile = new File("decoded");
        try {
            encoderOutputFile.createNewFile();
            InputStream encoderInputStream = new BufferedInputStream(new FileInputStream(encoderInputFile));
            final OutputStream encoderOutputStream =
                    new BufferedOutputStream(new FileOutputStream(encoderOutputFile));
            LZ77Encoder encoder = new LZ77Encoder();
            encoderInputStream = new BufferedInputStream(new FileInputStream(encoderInputFile));
            encoder.encode(15, 7, encoderInputStream, encoderOutputStream);
            encoderOutputStream.flush();
            encoderOutputStream.close();

            decoderOutputFile.createNewFile();
            InputStream decoderInputStream = new BufferedInputStream(new FileInputStream(encoderOutputFile));
            OutputStream decoderOutputStream = new BufferedOutputStream(new FileOutputStream(decoderOutputFile));
            LZ77Decoder decoder = new LZ77Decoder();
            decoder.decode(decoderInputStream, decoderOutputStream);
            decoderOutputStream.flush();
            decoderOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - time0);
        //endregion
    }
}
