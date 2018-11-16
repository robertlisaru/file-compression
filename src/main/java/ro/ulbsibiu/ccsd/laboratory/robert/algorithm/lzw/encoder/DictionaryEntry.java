package ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.encoder;

class DictionaryEntry {
    byte b;
    int left = Dictionary.NULL_INDEX;  //next entry with the same prefix as this entry, but lower byte value
    int right = Dictionary.NULL_INDEX; //next entry with the same prefix as this entry, but higher byte value
    int first = Dictionary.NULL_INDEX; //index of the first entry having this entry as prefix

}
