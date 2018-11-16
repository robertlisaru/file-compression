package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.decoder;

public class EmptyStrategyDictionary extends Dictionary {
    public EmptyStrategyDictionary(int nrBitsForIndex) {
        super(nrBitsForIndex);
    }

    @Override
    protected boolean evacuate() {
        size = 256;
        return size < table.length;
    }
}
