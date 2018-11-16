package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.decoder;

public class FreezeStrategyDictionary extends Dictionary {
    public FreezeStrategyDictionary(int nrBitsForIndex) {
        super(nrBitsForIndex);
    }

    @Override
    protected boolean evacuate() {
        return false;
    }
}
