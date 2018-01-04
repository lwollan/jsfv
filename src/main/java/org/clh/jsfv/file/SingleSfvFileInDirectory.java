package org.clh.jsfv.file;

import org.clh.jsfv.input.SfvFilenameFilter;

import java.io.File;
import java.io.FilenameFilter;

public class SingleSfvFileInDirectory {

    public static File locateSfvFileAndThrowExceptionIfMoreThanOneFileIsFound(File directory) {
        FilenameFilter filter = new SfvFilenameFilter();
        File[] listFiles = directory.listFiles(filter);
        if (listFiles != null) {
            if (listFiles.length == 1) {
                return listFiles[0];
            } else {
                if (listFiles.length > 1) {
                    // not suported
                    throw new IllegalArgumentException();
                }
            }
        }
        return directory;
    }
}
