package ro.ulbsibiu.ccsd.laboratory.robert.encryption;

import java.io.File;
import java.io.IOException;

public interface FileCompressionAlgorithm {
    public void compress(File inputFile, File outputFile) throws IOException;
}
