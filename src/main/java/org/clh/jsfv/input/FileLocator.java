package org.clh.jsfv.input;

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

        File[] entries = directory.listFiles();

        if (entries != null) {
            for (File entry : entries) {
                if (!entry.isHidden()) {
                    if (entry.isDirectory()) {
                        files.addAll(listFiles(entry));
                    } else {
                        if (filter.accept(entry.getParentFile(), entry.getName())) {
                            files.add(entry);
                        }
                    }
                }
            }
        }

        return files;
    }


}
