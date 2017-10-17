package org.clh.jsfv.file;

import org.clh.jsfv.logging.Event;
import org.clh.jsfv.logging.EventLogger;
import org.clh.jsfv.logging.StringEvent;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

public class SfvChecker {

    private File directory;
    private EventLogger evenHandler;

    public SfvChecker(String string) {
        this(new File(string));
    }

    SfvChecker(File directory) {
        if (directory.isFile()) {
            throw new IllegalArgumentException();
        }
        this.directory = directory;
    }

    public void process() throws IOException {
        List<File> sfvFiles = getFilesToProcess(directory);
        for (File file : sfvFiles) {
            processFile(file);
        }
    }

    private void processFile(File sfvFile) throws IOException {
        dispatchEvent(StringEvent.newFile(sfvFile));
        Long start = System.currentTimeMillis();

        SfvCheckDirectory sfvCheckDirectory = new SfvCheckDirectory(sfvFile.getParentFile());
        sfvCheckDirectory.process(evenHandler);

        Long processingTime = System.currentTimeMillis() - start;
        dispatchEvent(StringEvent.completed(sfvFile, processingTime));
    }

    public void setEventHandler(EventLogger eventHandler){
        this.evenHandler = eventHandler;
    }
    
    void dispatchEvent(Event event) {
        if (evenHandler != null) {
            evenHandler.log(event);
        }
    }

    private static List<File> getFilesToProcess(File directory) {
        FilenameFilter filter = new SfvFilenameFilter();
        FileLocator fileLocator = new FileLocator(filter);
        return fileLocator.listFiles(directory);
    }

}
