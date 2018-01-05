package org.clh.jsfv.input;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

public class FilesToProcess {

    private FilesToProcess() {}

    public static List<File> getListOfFiles(File directory) {
        FilenameFilter filter = new SfvFilenameFilter();
        FileLocator fileLocator = new FileLocator(filter);
        return fileLocator.listFiles(directory);
    }


}
