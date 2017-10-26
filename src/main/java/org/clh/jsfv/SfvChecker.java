package org.clh.jsfv;

import org.clh.jsfv.file.CheckAllFilesInDirectory;
import org.clh.jsfv.input.FilesToProcess;
import org.clh.jsfv.logging.Event;
import org.clh.jsfv.logging.Events;
import org.clh.jsfv.logging.logger.EventLogger;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class SfvChecker {

    private File directory;
    private EventLogger evenHandler;

    public SfvChecker(String string) {
        this(new File(string));
    }

    SfvChecker(File directory) {
        if (directory.isFile()) {
            throw new IllegalArgumentException();
        }
        this.directory = directory;
    }

    public void process() throws IOException {
        List<File> sfvFiles = FilesToProcess.getListOfFiles(directory);
        for (File file : sfvFiles) {
            processFile(file);
        }
    }

    private void processFile(File sfvFile) throws IOException {
        dispatchEvent(Events.newFile(sfvFile));
        LocalDateTime startTime = LocalDateTime.now();

        CheckAllFilesInDirectory checker = new CheckAllFilesInDirectory(sfvFile.getParentFile());
        checker.process(evenHandler);

        Duration duration = Duration.between(startTime, LocalDateTime.now());
        dispatchEvent(Events.completed(sfvFile, duration));
    }

    public void setEventHandler(EventLogger eventHandler) {
        this.evenHandler = eventHandler;
    }

    void dispatchEvent(Event event) {
        if (evenHandler != null) {
            evenHandler.log(event);
        }
    }

}
