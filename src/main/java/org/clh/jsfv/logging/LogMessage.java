package org.clh.jsfv.logging;

import java.io.File;

public final class LogMessage implements Event {

    private String event;

    private LogMessage(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return event;
    }

    public static Event newFile(File file) {
        return new LogMessage(String.format("Processing file: %s", file.toString()));
    }

    public static Event errorInFile(File file) {
        return new LogMessage(String.format("ERROR in: %s", file.toString()));
    }

    public static Event missingFile(File file) {
        return new LogMessage(String.format("MISSING FILE in: %s", file.toString()));
    }

    public static Event completed(File file, long processingTime) {
        return new LogMessage(String.format("Checked file %s in : %s ms", file.toString(), processingTime));
    }

    public static Event progress(File file, int count, int totalt) {
        return new LogMessage(String.format("Checked file %s in : %d %d", file.toString(), count, totalt));
    }
}
