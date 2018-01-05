package org.clh.jsfv.file;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;

public class FileOperations {

    static void renameToFailedFile(String parent, String filename) {
        new File(parent, filename)
                .renameTo(newFile(parent, filename, "%s-FAILED"));
    }

    private static void createdFailedFile(String parent, String filename) throws IOException {
        File failedFile = newFile(parent, filename, "%s-FAILED");
        if (!failedFile.exists()) {
            failedFile.createNewFile();
        }
    }

    public static void removeFailedFile(String parent, String filename) {
        File failedFile = newFile(parent, filename, "%s-FAILED");
        if (failedFile.exists()) {
            failedFile.delete();
        }
    }

    public static void removeMissingFile(String parent, String filename) {
        File missingFile = newFile(parent, filename, "%s-MISSING");
        if (missingFile.exists()) {
            missingFile.delete();
        }
    }

    public static void createMissingFile(String parent, String filename) throws IOException {
        File missingFile = newFile(parent, filename, "%s-MISSING");
        if (!missingFile.exists()) {
            missingFile.createNewFile();
        }
    }

    private static File newFile(String parent, String filename, String format) {
        return new File(parent, format(format, filename));
    }
}
