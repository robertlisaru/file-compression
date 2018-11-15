package ro.ulbsibiu.ccsd.laboratory.robert.test.encoding.lzw;

import org.junit.jupiter.api.Test;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.lzw.Dictionary;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.lzw.FreezeStrategyDictionary;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DictionaryTest {
    @Test
    void simpleSearch() {
        Dictionary dictionary = new FreezeStrategyDictionary(9);
        assertEquals(0, dictionary.search(Dictionary.EMPTY_PREFIX, (byte) 0));
        assertEquals(Dictionary.NULL_INDEX, dictionary.search(0, (byte) 7));
        assertEquals(7, dictionary.search(Dictionary.EMPTY_PREFIX, (byte) 7));
        assertEquals(Dictionary.NULL_INDEX, dictionary.search(7, (byte) 0));
        assertEquals(0, dictionary.search(Dictionary.EMPTY_PREFIX, (byte) 0));
        assertEquals(256, dictionary.search(0, (byte) 7));
    }

    @Test
    void advancedSearch() {
        Dictionary dictionary = new FreezeStrategyDictionary(9);
        final byte[] bytes = new String("ABCDABCDABDCABCDABDDCD").getBytes(StandardCharsets.US_ASCII);
        //region search expectations array
        int[] searchExpectations = new int[]{
                'A',
                Dictionary.NULL_INDEX, //AB
                'B',
                Dictionary.NULL_INDEX, //BC
                'C',
                Dictionary.NULL_INDEX, //CD
                'D',
                Dictionary.NULL_INDEX, //DA
                'A',
                256, //AB
                Dictionary.NULL_INDEX, //ABC
                'C',
                258, //CD
                Dictionary.NULL_INDEX, //CDA
                'A',
                256, //AB
                Dictionary.NULL_INDEX, //ABD
                'D',
                Dictionary.NULL_INDEX, //DC
                'C',
                Dictionary.NULL_INDEX, //CA
                'A',
                256, //AB
                260, //ABC
                Dictionary.NULL_INDEX, //ABCD
                'D',
                259, //DA
                Dictionary.NULL_INDEX, //DAB
                'B',
                Dictionary.NULL_INDEX, //BD
                'D',
                Dictionary.NULL_INDEX, //DD
                'D',
                263, //DC
                Dictionary.NULL_INDEX, //DCD
                'D'
        };
        //endregion

        int prefix = Dictionary.EMPTY_PREFIX;
        int numOfSearches = 0;
        for (int i = 0; i < bytes.length; i++) {
            int index = dictionary.search(prefix, bytes[i]);
            assertEquals(searchExpectations[numOfSearches++], index);
            if (index == Dictionary.NULL_INDEX) {
                i--;
            }
            prefix = index;
        }
    }

    @Test
    void isFull() {
        Dictionary dictionary = new FreezeStrategyDictionary(8);
        assertTrue(dictionary.isFull());
        dictionary = new FreezeStrategyDictionary(9);
        assertFalse(dictionary.isFull());
    }
}
