package org.clh.jsfv.logging.processing;

public class FileMissingEvent extends BaseFileProcessedEvent {

    public FileMissingEvent(String directory, String file) {
        super(directory, file);
    }

}
