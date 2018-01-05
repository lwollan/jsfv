package org.clh.jsfv.logging.processing;

public class FileFailedEvent extends BaseFileProcessedEvent {

    public FileFailedEvent(String directory, String file) {
        super(directory, file);
    }

}
