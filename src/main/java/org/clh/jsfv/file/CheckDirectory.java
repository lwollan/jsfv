package org.clh.jsfv.file;

import org.clh.jsfv.crc32.Stream;
import org.clh.jsfv.logging.Events;
import org.clh.jsfv.logging.logger.EventLogger;
import org.clh.jsfv.state.StateFile;

import java.io.*;
import java.util.Map;

import static org.clh.jsfv.file.ProcessingResult.fileMissing;
import static org.clh.jsfv.file.ProcessingResult.fileProcessedOK;
import static org.clh.jsfv.file.ProcessingResult.fileProcessedWithError;
import static org.clh.jsfv.file.SingleSfvFileInDirectory.locateSfvFileAndThrowExceptionIfMoreThanOneFileIsFound;

public class CheckDirectory {

    private File sfvFile;
    private File directory;

    public CheckDirectory(File directory) throws IllegalArgumentException {
        if (directory.isFile()) {
            throw new IllegalArgumentException();
        }
        this.directory = directory;

        sfvFile = locateSfvFileAndThrowExceptionIfMoreThanOneFileIsFound(this.directory);
    }

    public void process(EventLogger evenHandler) throws IOException {
        Map<String, Long> entries = SfvFile.readSfvFile(sfvFile);
        procesesEntries(entries, directory);

        if (getNumberOfFailedFiles(entries, directory) > 0) {
            evenHandler.log(Events.errorInFile(sfvFile));
        }

        if (getNumberOfMissingFiles(entries, directory) > 0) {
            evenHandler.log(Events.missingFile(sfvFile));
        }

    }

    private static void procesesEntries(Map<String, Long> map, File directory) throws IOException {
        StateFile stateFile = new StateFile(directory, map.size());

        for (String file : map.keySet()) {
            processFile(stateFile, directory, file, map);
        }

        stateFile.setFinalStatus();
    }

    private static void processFile(StateFile headerfile, File directory, String file, Map<String, Long> entries) throws IOException {
        if (fileExists(directory, file)) {
            if (isCheckSumOK(directory, file, entries)) {
                fileProcessedOK(headerfile, directory, file);
            } else {
                fileProcessedWithError(headerfile, directory, file);
            }
        } else {
            fileMissing(headerfile, directory, file);
        }
        headerfile.update();
    }

    private static boolean fileExists(File parentDirectory, String file) {
        return new File(parentDirectory, file).exists();
    }

    private static boolean isCheckSumOK(File directory, String file, Map<String, Long> map) throws IOException {
        long calculateCRC32 = Stream.calculateCRC32(new File(directory, file));
        long expectedCRC32 = map.get(file);

        return isChecksumAsExpected(calculateCRC32, expectedCRC32);
    }

    private static boolean isChecksumAsExpected(long calculatedChecksum, Long expectedChecksum) {
        return expectedChecksum.equals(calculatedChecksum);
    }

    static int getNumberOfMissingFiles(Map<String, Long> map, File directory) {
        int count = 0;
        for (String file : map.keySet()) {
            if (!fileExists(directory, file)) {
                count++;
            }
        }
        return count;
    }

    static int getNumberOfFailedFiles(Map<String, Long> map, File directory) {
        int count = 0;
        for (String file : map.keySet()) {
            if (fileExists(directory, file + "-FAILED")) {
                count++;
            }
        }
        return count;
    }

}
