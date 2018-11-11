package ro.ulbsibiu.ccsd.laboratory.robert.encoding.lz77;

import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;

import java.io.OutputStream;

public class LZ77Encoder {
    private BitWriter bitWriter;

    public LZ77Encoder(BitWriter bitWriter) {
        this.bitWriter = bitWriter;
    }

    public void encode(OutputStream outputStream) {

    }
}
