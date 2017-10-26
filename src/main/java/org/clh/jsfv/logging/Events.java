package org.clh.jsfv.logging;

import java.io.File;
import java.time.Duration;

public class Events {

    private Events() {

    }

    public static Event newFile(File file) {
        return new LogMessage(String.format("Processing file: %s.", file.toString()));
    }

    public static Event errorInFile(File file) {
        return new LogMessage(String.format("ERROR in: %s.", file.toString()));
    }

    public static Event missingFile(File file) {
        return new LogMessage(String.format("MISSING FILE in: %s.", file.toString()));
    }

    public static Event completed(File file, Duration processingTime) {
        return new LogMessage(String.format("Checked file %s in : %s seconds.", file.toString(), processingTime.getSeconds()));
    }

    public static Event progress(File file, int count, int totalt, long processingTime) {
        return new LogMessage(String.format("Checked file %s in : %d %d in : %s seconds.", file.toString(), count, totalt));
    }
}
