package org.clh.jsfv.file;

import org.clh.jsfv.crc32.Stream;
import org.clh.jsfv.logging.EventLogger;
import org.clh.jsfv.logging.LogMessage;
import org.clh.jsfv.state.StateFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class SfvCheckDirectory {

    private File sfvFile;
    private File directory;

    private Map<String, Long> map = new HashMap<String, Long>();
    private int failedFileCount;

    public SfvCheckDirectory(File directory) throws IOException {
        if (directory.isFile()) {
            throw new IllegalArgumentException();
        }
        this.directory = directory;

        locateSfvFileAndThrowExceptionIfMoreThanOneFileIsFound(directory);
    }


    private void locateSfvFileAndThrowExceptionIfMoreThanOneFileIsFound(
            File directory) {
        FilenameFilter filter = new SfvFilenameFilter();
        File[] listFiles = directory.listFiles(filter);
        if (listFiles != null) {
            if (listFiles.length == 1) {
                sfvFile = listFiles[0];
            } else {
                if (listFiles.length > 1) {
                    // not suported
                    throw new IllegalArgumentException();
                }
            }
        }
    }


    public void process(EventLogger evenHandler) throws IOException {
        readSfvFileIntoFileMap();
        procesesEntriesInFilemap();

        if (getNumberOfFailedFiles() > 0) {
            evenHandler.log(LogMessage.errorInFile(sfvFile));
        }

        if (getNumberOfMissingFiles() > 0) {
            evenHandler.log(LogMessage.missingFile(sfvFile));
        }

    }


    private void procesesEntriesInFilemap() throws FileNotFoundException,
            IOException {
        StateFile headerfile = new StateFile(directory, map.size());

        for (String file : map.keySet()) {
            processFile(headerfile, file);
        }

        headerfile.setFinalStatus();
    }


    private void readSfvFileIntoFileMap() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(sfvFile)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (isCommentLine(line)) {
                continue;
            } else {
                processSfvLine(line);
            }
        }
    }


    private void processSfvLine(String line) {
        String filePart = line.substring(0, line.lastIndexOf(" ")).trim();
        String crc16 = line.substring(line.lastIndexOf(" ")).trim();
        long sfv = Long.parseLong(crc16, 16);
        map.put(filePart, sfv);
    }


    private boolean isCommentLine(String line) {
        return line.startsWith(";");
    }


    private void processFile(StateFile headerfile, String file) {
        try {
            if (new File(directory, file).exists()) {

                long calculateCRC32 = Stream.calculateCRC32(new File(directory, file));
                long expectedCRC32 = map.get(file);

                if (!isChecksumAsExpected(calculateCRC32, expectedCRC32)) {
                    renameToFailedFile(file);
                    headerfile.incrementFailedCount();
                } else {
                    headerfile.incrementCount();
                    removeMissingFile(file);
                    removeFailedFile(file);
                }
                headerfile.update();
            } else {
                headerfile.incrementMissingCount();
                createMissingFile(file);
            }
            headerfile.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renameToFailedFile(String file) {
        new File(directory, file)
                .renameTo(new File(directory, file + "-FAILED"));
    }

    private void createdFailedFile(String file) throws IOException {
        File failedFile = new File(directory, file + "-FAILED");
        if (!failedFile.exists()) {
            failedFile.createNewFile();
        }
    }

    private void removeMissingFile(String file) {
        File missingFile = new File(directory, file + "-MISSING");
        if (missingFile.exists()) {
            missingFile.delete();
        }
    }

    private void removeFailedFile(String file) {
        File failedFile = new File(directory, file + "-FAILED");
        if (failedFile.exists()) {
            failedFile.delete();
        }
    }

    private void createMissingFile(String file) throws IOException {
        File missingFile = new File(directory, file + "-MISSING");
        if (!missingFile.exists()) {
            missingFile.createNewFile();
        }
    }

    public int getNumberOfMissingFiles() {
        int count = 0;
        for (String file : map.keySet()) {
            if (!new File(directory, file).exists()) {
                count++;
            }
        }
        return count;
    }

    public int getNumberOfFailedFiles() throws IOException {
        return failedFileCount;
    }

    private boolean isChecksumAsExpected(long calculatedChecksum, Long expectedChecksum) {
        return expectedChecksum.equals(calculatedChecksum);
    }


}
