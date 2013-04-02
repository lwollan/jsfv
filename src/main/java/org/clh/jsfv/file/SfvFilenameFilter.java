package org.clh.jsfv.file;

import java.io.File;
import java.io.FilenameFilter;

final class SfvFilenameFilter implements FilenameFilter {
    private static final String SUFFIX = ".sfv";

    @Override
    public boolean accept(File dir, String name) {
        if (name != null && name.toUpperCase().endsWith(SUFFIX.toUpperCase())) {
            return true;
        }
        return false;
    }
}