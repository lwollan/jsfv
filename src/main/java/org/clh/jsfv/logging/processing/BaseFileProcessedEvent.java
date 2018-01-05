package org.clh.jsfv.logging.processing;

public abstract class BaseFileProcessedEvent implements ProcessingEvent {
    private final String directory;
    private final String file;

    protected BaseFileProcessedEvent(String directory, String file) {
        this.directory = directory;
        this.file = file;
    }

    @Override
    public String getDirectory() {
        return directory;
    }

    @Override
    public String getFilename() {
        return file;
    }

}
