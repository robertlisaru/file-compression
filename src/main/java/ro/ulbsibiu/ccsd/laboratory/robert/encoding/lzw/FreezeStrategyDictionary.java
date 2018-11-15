package ro.ulbsibiu.ccsd.laboratory.robert.encoding.lzw;

public class FreezeStrategyDictionary extends Dictionary {

    public FreezeStrategyDictionary(int nrBitsForIndex) {
        super(nrBitsForIndex);
    }

    @Override
    protected boolean evacuate() {
        return false; //do nothing & the dictionary will stay full
    }
}
