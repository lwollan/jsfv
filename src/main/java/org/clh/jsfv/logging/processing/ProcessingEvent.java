package org.clh.jsfv.logging.processing;

import org.clh.jsfv.logging.Event;

public interface ProcessingEvent extends Event {

    String getDirectory();

    String getFilename();
}
