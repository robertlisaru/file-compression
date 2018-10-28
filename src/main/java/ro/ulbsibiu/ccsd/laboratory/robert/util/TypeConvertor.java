package ro.ulbsibiu.ccsd.laboratory.robert.util;

import java.util.BitSet;

public class TypeConvertor {
    public static BitSet asBitSet(byte value) {
        return BitSet.valueOf(new byte[]{value});
    }

    public static byte[] asByteArray(int integer) {
        return new byte[]{(byte) integer};
    }
}
