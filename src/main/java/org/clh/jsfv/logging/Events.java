package org.clh.jsfv.logging;

import java.io.File;
import java.time.Duration;

public class Events {

    private Events() {

    }

    public static Event newFile(String filename) {
        return new LogMessage(String.format("Processing file: %s.", filename));
    }

    public static Event errorInFile(String filename) {
        return new LogMessage(String.format("ERROR in: %s.", filename));
    }

    public static Event missingFile(String filename) {
        return new LogMessage(String.format("MISSING FILE in: %s.", filename));
    }

    public static Event processedFile(String filename, Duration duration) {
        return new LogMessage(String.format("Processed %s OK in %s seconds.", filename, duration.getSeconds()));
    }

    public static Event completed(File file, Duration processingTime) {
        return new LogMessage(String.format("Checked file %s in : %s seconds.", file.toString(), processingTime.getSeconds()));
    }

}
