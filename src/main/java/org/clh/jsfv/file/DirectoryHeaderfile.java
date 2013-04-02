package org.clh.jsfv.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class DirectoryHeaderfile {

    private File directory;

    private Integer checkedCount = 0;
    private Integer totaltCount;

    private int failedCount = 0;

    private int missingCount = 0;

    public DirectoryHeaderfile(File directory, Integer totalCount) {
        super();
        this.directory = directory;
        this.totaltCount = totalCount;
    }

    public void incrementCount() {
        checkedCount++;
    }

    // -[site]-_COMPLETE_100%_NN_OF_NN_FILES_[site]-
    public void update() throws IOException {
        File headerFile = getHeaderFile();
        File newHeaderFile = new File(directory, createFilename());
        headerFile.renameTo(newHeaderFile);
    }

    private File getHeaderFile() throws IOException {
        File headerFile = locateHeaderFile("jsfv");
        if (headerFile == null) {
            headerFile = new File("dummy");
            headerFile.createNewFile();
        }
        return headerFile;
    }

    private String createFilename() {
        return "-[jsfv]_UNKNOWN_"+ getCompletedPercent()+"%_" + (getOKCount()) + "_OF_" + totaltCount
                + "_FILES_[jsfv]-";
    }

    private File locateHeaderFile(final String string) {
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if (name.startsWith("-[" + string + "]")) {
                    return true;
                }
                return false;
            }
        };
        File[] files = directory.listFiles(filter);
        if (files == null || files.length == 0) {
            return null;
        } else {
            return files[0];
        }
    }

    public void incrementFailedCount() {
        this.failedCount ++;
        
    }

    public void setFinalStatus() throws IOException {
        File headerFile = getHeaderFile();
        File newHeaderFile = null;
        if (failedCount == 0 && missingCount == 0) {
            newHeaderFile = new File(directory, createStateOkFilename());
        } else {
            newHeaderFile = new File(directory, createSateIncompleteFilename());
        }
        headerFile.renameTo(newHeaderFile);
    }

    private String createSateIncompleteFilename() {
        return "-[jsfv]_INCOMPLETE_"+getCompletedPercent()+"%_" + (getOKCount()) + "_OF_" + totaltCount
        + "_FILES_[jsfv]-";
    }

    private int getCompletedPercent() {
        double a =  (getOKCount()/(double)totaltCount)*100;
        return (int)a;
    }

    private int getOKCount() {
        return checkedCount;
    }

    private String createStateOkFilename() {
        return "-[jsfv]_OK_"+getCompletedPercent()+"%_" + (getOKCount()) + "_OF_" + totaltCount
        + "_FILES_[jsfv]-";
    }

    public void incrementMissingCount() {
        this.missingCount  ++;
    }

}
