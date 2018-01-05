package org.clh.jsfv.file;

import org.clh.jsfv.crc32.Stream;
import org.clh.jsfv.handler.StateHandler;
import org.clh.jsfv.logging.logger.EventLogger;
import org.clh.jsfv.logging.processing.FileFailedEvent;
import org.clh.jsfv.logging.processing.FileMissingEvent;
import org.clh.jsfv.logging.processing.FileProcessedEvent;
import org.clh.jsfv.logging.processing.ProcessingEvent;
import org.clh.jsfv.state.StateFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

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
        StateFile stateFile = new StateFile(directory, entries.size());
        StateHandler stateHandler = new StateHandler(stateFile, evenHandler);
        procesesEntries(stateHandler, entries, directory);

        stateHandler.close();
    }

    private static void procesesEntries(StateHandler stateHandler, Map<String, Long> map, File directory) throws IOException {
        for (String file : map.keySet()) {
            LocalDateTime start = LocalDateTime.now();
            ProcessingEvent event = processFile(directory, file, map);
            stateHandler.handle(event, Duration.between(start, LocalDateTime.now()));
        }
    }

    private static ProcessingEvent processFile(File directory, String file, Map<String, Long> entries) throws IOException {
        if (fileExists(directory, file)) {
            if (isCheckSumOK(directory, file, entries)) {
                return new FileProcessedEvent(directory.getCanonicalPath(), file);
            } else {
                return new FileFailedEvent(directory.getCanonicalPath(), file);
            }
        } else {
            return new FileMissingEvent(directory.getCanonicalPath(), file);
        }
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
