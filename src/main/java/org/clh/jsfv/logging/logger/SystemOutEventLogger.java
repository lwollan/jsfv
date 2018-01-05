package org.clh.jsfv.logging.logger;

import org.clh.jsfv.logging.Event;

import java.io.PrintStream;

public class SystemOutEventLogger implements EventLogger {

    private PrintStream out;

    public SystemOutEventLogger() {
        this(System.out);
    }

    public SystemOutEventLogger(PrintStream out) {
        this.out = out;
    }

    @Override
    public void log(Event event) {
        out.println(event.toString());
    }

}
