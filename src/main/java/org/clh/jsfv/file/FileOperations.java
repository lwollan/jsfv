package org.clh.jsfv.file;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;

public class FileOperations {

    static void renameToFailedFile(File parent, String filename) {
        new File(parent, filename)
                .renameTo(newFile(parent, filename, "%s-FAILED"));
    }

    private static void createdFailedFile(File parent, String filename) throws IOException {
        File failedFile = newFile(parent, filename, "%s-FAILED");
        if (!failedFile.exists()) {
            failedFile.createNewFile();
        }
    }

    static void removeFailedFile(File parent, String filename) {
        File failedFile = newFile(parent, filename, "%s-FAILED");
        if (failedFile.exists()) {
            failedFile.delete();
        }
    }

    static void removeMissingFile(File parent, String filename) {
        File missingFile = newFile(parent, filename, "%s-MISSING");
        if (missingFile.exists()) {
            missingFile.delete();
        }
    }

    static void createMissingFile(File parent, String filename) throws IOException {
        File missingFile = newFile(parent, filename, "%s-MISSING");
        if (!missingFile.exists()) {
            missingFile.createNewFile();
        }
    }

    private static File newFile(File parent, String filename, String format) {
        return new File(parent, format(format, filename));
    }
}
