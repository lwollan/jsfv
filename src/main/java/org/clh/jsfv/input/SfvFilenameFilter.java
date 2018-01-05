package org.clh.jsfv.input;

import java.io.File;
import java.io.FilenameFilter;

public class SfvFilenameFilter implements FilenameFilter {
    private static final String SUFFIX = ".sfv";

    @Override
    public boolean accept(File dir, String name) {
        if (name != null && name.toUpperCase().endsWith(SUFFIX.toUpperCase())) {
            return true;
        }
        return false;
    }
}