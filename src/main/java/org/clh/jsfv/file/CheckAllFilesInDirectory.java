package org.clh.jsfv.file;

import org.clh.jsfv.crc32.Stream;
import org.clh.jsfv.input.SfvFilenameFilter;
import org.clh.jsfv.logging.Events;
import org.clh.jsfv.logging.logger.EventLogger;
import org.clh.jsfv.state.StateFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.clh.jsfv.file.FileOperations.*;

public class CheckAllFilesInDirectory {

    private File sfvFile;
    private File directory;

    private Map<String, Long> map = new HashMap<>();
    private int failedFileCount;

    public CheckAllFilesInDirectory(File directory) throws IOException {
        if (directory.isFile()) {
            throw new IllegalArgumentException();
        }
        this.directory = directory;

        sfvFile = locateSfvFileAndThrowExceptionIfMoreThanOneFileIsFound(directory);
    }

    public void process(EventLogger evenHandler) throws IOException {
        readSfvFileIntoFileMap();
        procesesEntriesInFilemap();

        if (getNumberOfFailedFiles() > 0) {
            evenHandler.log(Events.errorInFile(sfvFile));
        }

        if (getNumberOfMissingFiles() > 0) {
            evenHandler.log(Events.missingFile(sfvFile));
        }

    }

    private void procesesEntriesInFilemap() throws IOException {
        StateFile headerfile = new StateFile(directory, map.size());

        for (String file : map.keySet()) {
            processFile(headerfile, file);
        }

        headerfile.setFinalStatus();
    }

    private void readSfvFileIntoFileMap() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sfvFile)))) {
            reader.lines()
                    .filter(line -> !isCommentLine(line))
                    .map(CheckAllFilesInDirectory::getFileNameAndCRC)
                    .forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        }
    }

    private void processFile(StateFile headerfile, String file) throws IOException {
        if (new File(directory, file).exists()) {
            if (isCheckSumOK(file)) {
                headerfile.incrementCount();
                removeMissingFile(directory, file);
                removeFailedFile(directory, file);
            } else {
                renameToFailedFile(directory, file);
                headerfile.incrementFailedCount();
            }
            headerfile.update();
        } else {
            headerfile.incrementMissingCount();
            createMissingFile(directory, file);
        }
        headerfile.update();
    }

    private boolean isCheckSumOK(String file) throws IOException {
        long calculateCRC32 = Stream.calculateCRC32(new File(directory, file));
        long expectedCRC32 = map.get(file);

        return isChecksumAsExpected(calculateCRC32, expectedCRC32);
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

    private static File locateSfvFileAndThrowExceptionIfMoreThanOneFileIsFound(File directory) {
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

    private static Map.Entry<String, Long> getFileNameAndCRC(String jsfvLine) {
        String filePart = jsfvLine.substring(0, jsfvLine.lastIndexOf(" ")).trim();
        String crc16 = jsfvLine.substring(jsfvLine.lastIndexOf(" ")).trim();
        long sfv = Long.parseLong(crc16, 16);
        return new HashMap.SimpleEntry<>(filePart, sfv);
    }

    private static boolean isCommentLine(String line) {
        return line.startsWith(";");
    }

    private static boolean isChecksumAsExpected(long calculatedChecksum, Long expectedChecksum) {
        return expectedChecksum.equals(calculatedChecksum);
    }

}
