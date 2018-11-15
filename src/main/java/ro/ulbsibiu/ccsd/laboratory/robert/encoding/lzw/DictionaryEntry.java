package ro.ulbsibiu.ccsd.laboratory.robert.encoding.lzw;

class DictionaryEntry {
    byte b;
    int left = -1;  //next entry with the same prefix as this entry, but lower byte value
    int right = -1; //next entry with the same prefix as this entry, but higher byte value
    int first = -1; //index of the first entry having this entry as prefix

}
