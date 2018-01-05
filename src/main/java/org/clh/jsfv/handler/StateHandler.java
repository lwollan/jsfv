package org.clh.jsfv.handler;

import org.clh.jsfv.logging.Events;
import org.clh.jsfv.logging.logger.EventLogger;
import org.clh.jsfv.logging.processing.FileFailedEvent;
import org.clh.jsfv.logging.processing.FileMissingEvent;
import org.clh.jsfv.logging.processing.FileProcessedEvent;
import org.clh.jsfv.logging.processing.ProcessingEvent;
import org.clh.jsfv.state.StateFile;

import java.io.IOException;
import java.time.Duration;

import static org.clh.jsfv.file.FileOperations.createMissingFile;
import static org.clh.jsfv.file.FileOperations.removeFailedFile;
import static org.clh.jsfv.file.FileOperations.removeMissingFile;

public class StateHandler {

    private StateFile stateFile;
    private EventLogger eventLogger;

    public StateHandler(StateFile stateFile, EventLogger eventLogger) {
        this.stateFile = stateFile;
        this.eventLogger = eventLogger;
    }

    public void handle(ProcessingEvent event, Duration duration) throws IOException {
        if (event instanceof FileFailedEvent) {
            createMissingFile(event.getDirectory(), event.getFilename());
            eventLogger.log(Events.errorInFile(event.getFilename()));
            stateFile.incrementFailedCount();
        } else if (event instanceof FileMissingEvent) {
            createMissingFile(event.getDirectory(), event.getFilename());
            stateFile.incrementMissingCount();
            eventLogger.log(Events.missingFile(event.getFilename()));
        } else if (event instanceof FileProcessedEvent) {
            removeMissingFile(event.getDirectory(), event.getFilename());
            removeFailedFile(event.getDirectory(), event.getDirectory());
            eventLogger.log(Events.processedFile(event.getFilename(), duration));
            stateFile.incrementCount();
        }

        stateFile.update();
    }

    public void close() throws IOException {
        stateFile.setFinalStatus();
    }
}
