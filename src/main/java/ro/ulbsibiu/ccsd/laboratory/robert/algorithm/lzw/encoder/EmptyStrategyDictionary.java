package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.encoder;

public class EmptyStrategyDictionary extends Dictionary {

    public EmptyStrategyDictionary(int nrBitsForIndex) {
        super(nrBitsForIndex);
    }

    @Override
    protected boolean evacuate() {
        for (int i = 0; i < 256; i++) {
            table[i].first = Dictionary.NULL_INDEX;
        }
        size = 256;
        return size < table.length;
    }

    @Override
    public long getHeaderCode() {
        return 1;
    }
}
