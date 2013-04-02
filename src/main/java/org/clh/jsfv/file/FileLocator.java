package org.clh.jsfv.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class FileLocator {

    private final FilenameFilter filter;

    public FileLocator(FilenameFilter filter) {
        this.filter = filter;
    }

    public List<File> listFiles(File directory) {
        List<File> files = new ArrayList<File>();

        File[] entries = directory.listFiles(filter);

        if (entries != null) {
            for (File entry : entries) {
                files.add(entry);

                if (entry.isDirectory()) {
                    files.addAll(listFiles(entry));
                }
            }
        }

        return files;
    }


}
