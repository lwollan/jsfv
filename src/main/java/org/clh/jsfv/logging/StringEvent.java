package org.clh.jsfv.logging;

import java.io.File;

public final class StringEvent implements Event {

    private String event;

    private StringEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return event;
    }

    public static Event newFile(File file) {
        return new StringEvent("Processing file :" + file.toString());
    }

    public static Event errorInFile(File file) {
        return new StringEvent("ERROR in :" + file.toString());
    }

    public static Event missingFile(File file) {
        return new StringEvent("MISSING FILE in :" + file.toString());
    }

    public static Event completed(File file, long processingTime) {
        return new StringEvent(String.format("Checked file %s in : %s ms", file.toString(), processingTime));
    }

}
