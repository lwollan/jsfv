package org.clh.jsfv.file;

import org.clh.jsfv.state.StateFile;

import java.io.File;
import java.io.IOException;

import static org.clh.jsfv.file.FileOperations.*;

class ProcessingResult {

    static void fileMissing(StateFile headerfile, File directory, String file) throws IOException {
        createMissingFile(directory, file);
        headerfile.incrementMissingCount();
    }

    static void fileProcessedWithError(StateFile headerfile, File directory, String file) {
        renameToFailedFile(directory, file);
        headerfile.incrementFailedCount();
    }

    static void fileProcessedOK(StateFile headerfile, File directory, String file) {
        removeMissingFile(directory, file);
        removeFailedFile(directory, file);
        headerfile.incrementCount();
    }
}
