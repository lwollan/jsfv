package org.clh.jsfv.state;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class StateFile {

    public static final String SITE_NAME = "jsfv";
    private File directory;

    private int checkedCount = 0;
    private int totaltCount;
    private int failedCount = 0;
    private int missingCount = 0;

    public StateFile(File directory, Integer totalCount) {
        super();
        this.directory = directory;
        this.totaltCount = totalCount;
    }

    public void incrementCount() {
        checkedCount++;
    }

    public void update() throws IOException {
        File headerFile = getHeaderFile();
        File newState = new File(directory, State.UNKNOWN(checkedCount, totaltCount));
        headerFile.renameTo(newState);
    }

    private File getHeaderFile() throws IOException {
        File headerFile = locateHeaderFile(SITE_NAME);
        if (headerFile == null) {
            headerFile = new File("dummy");
            headerFile.createNewFile();
        }
        return headerFile;
    }

    private File locateHeaderFile(final String siteName) throws IOException {
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if (name.startsWith("-[" + siteName + "]")) {
                    return true;
                }
                return false;
            }
        };

        File[] files = directory.listFiles(filter);
        if (files == null || files.length == 0) {
            File dummy = new File(directory, "dummy");
            dummy.createNewFile();
            return dummy;
        } else {
            return files[0];
        }
    }

    public void incrementFailedCount() {
        this.failedCount++;

    }

    public void incrementMissingCount() {
        this.missingCount++;
    }

    public void setFinalStatus() throws IOException {
        File headerFile = getHeaderFile();

        File newHeaderFile;
        if (failedCount == 0 && missingCount == 0) {
            newHeaderFile = new File(directory, State.OK(checkedCount, totaltCount));
        } else {
            newHeaderFile = new File(directory, State.INCOMPLETE(checkedCount, totaltCount));
        }
        headerFile.renameTo(newHeaderFile);
    }

}
