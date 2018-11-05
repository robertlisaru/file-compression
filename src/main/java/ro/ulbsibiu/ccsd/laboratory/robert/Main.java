package ro.ulbsibiu.ccsd.laboratory.robert;

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
        
    }
}
