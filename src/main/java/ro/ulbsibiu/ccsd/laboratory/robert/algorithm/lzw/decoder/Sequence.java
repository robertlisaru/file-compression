package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.decoder;

public class Sequence {
    final byte[] array;
    int size;

    public Sequence(int nrBitsForIndex) {
        array = new byte[1 << nrBitsForIndex];
        size = 0;
    }
}
