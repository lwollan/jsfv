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

    public CheckAllFilesInDirectory(File directory) throws IllegalArgumentException {
        if (directory.isFile()) {
            throw new IllegalArgumentException();
        }
        this.directory = directory;

        sfvFile = locateSfvFileAndThrowExceptionIfMoreThanOneFileIsFound(this.directory);
    }

    public void process(EventLogger evenHandler) throws IOException {
        Map<String, Long> entries = readSfvFile(sfvFile);
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
            processFile(stateFile, file, directory, map);
        }

        stateFile.setFinalStatus();
    }

    private static Map<String, Long> readSfvFile(File sfvFile) throws IOException {
        Map<String, Long> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sfvFile)))) {
            reader.lines()
                    .filter(line -> !isCommentLine(line))
                    .map(CheckAllFilesInDirectory::getFileNameAndCRC)
                    .forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        }
        return map;
    }

    private static void processFile(StateFile headerfile, String file, File directory, Map<String, Long> entries) throws IOException {
        if (fileExists(directory, file)) {
            if (isCheckSumOK(file, entries, directory)) {
                fileProcessedOK(headerfile, file, directory);
            } else {
                fileProcessedWithError(headerfile, file, directory);
            }
        } else {
            fileMissing(headerfile, file, directory);
        }
        headerfile.update();
    }

    private static void fileMissing(StateFile headerfile, String file, File directory) throws IOException {
        createMissingFile(directory, file);
        headerfile.incrementMissingCount();
    }

    private static void fileProcessedWithError(StateFile headerfile, String file, File directory) {
        renameToFailedFile(directory, file);
        headerfile.incrementFailedCount();
    }

    private static void fileProcessedOK(StateFile headerfile, String file, File directory) {
        removeMissingFile(directory, file);
        removeFailedFile(directory, file);
        headerfile.incrementCount();
    }

    private static boolean fileExists(File parentDirectory, String file) {
        return new File(parentDirectory, file).exists();
    }

    private static boolean isCheckSumOK(String file, Map<String, Long> map, File directory) throws IOException {
        long calculateCRC32 = Stream.calculateCRC32(new File(directory, file));
        long expectedCRC32 = map.get(file);

        return isChecksumAsExpected(calculateCRC32, expectedCRC32);
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
