package org.clh.jsfv.logging;

public final class LogMessage implements Event {

    private String event;

    LogMessage(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return event;
    }

}
