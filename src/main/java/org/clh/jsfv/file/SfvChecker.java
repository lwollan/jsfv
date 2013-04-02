package org.clh.jsfv.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;

public class SfvChecker {

    final class StringSfvCheckerEvent implements SfvCheckerEvent {
        
        private String event;
        public StringSfvCheckerEvent(String event) {
            this.event = event;
        }
        @Override
        public String toString() {
            return event;
        }
    }

    private File file;
    private SfvCheckerEventHandler evenHandler;

    public SfvChecker(String string) {
        this(new File(string));
    }

    SfvChecker(File file) {
        if (file.isFile()) {
            throw new IllegalArgumentException();
        }
        this.file = file;
    }

    public void process() throws IOException {
        FilenameFilter filter = new SfvFilenameFilter();
        FileLocator fileLocator = new FileLocator(filter);

        List<File> sfvFiles = fileLocator.listFiles(file);
        for (File file : sfvFiles) {
            processFile(file);
        }
    }

    private void processFile(File file) throws IOException {
        dispatchEvent(new StringSfvCheckerEvent("Processing file :" + file.toString()));

        SfvCheckDirectory sfvCheckDirectory = new SfvCheckDirectory(file.getParentFile());
        sfvCheckDirectory.process();

        if (sfvCheckDirectory.getNumberOfFailedFiles() > 0) {
            dispatchEvent(new StringSfvCheckerEvent("ERROR in :" + file.toString()));
        }

        if (sfvCheckDirectory.getNumberOfMissingFiles() > 0) {
            dispatchEvent(new StringSfvCheckerEvent("MISSING FILE in :" + file.toString()));
        }
    }

    public void setEventHandler(SfvCheckerEventHandler eventHandler){
        this.evenHandler = eventHandler;
    }
    
    void dispatchEvent(SfvCheckerEvent event) {
        if (evenHandler != null) {
            evenHandler.handle(event);
        }
    }
    
    public static void main(String args[]) throws Exception {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int openDialog = chooser.showOpenDialog(null);
        if (openDialog == JFileChooser.APPROVE_OPTION) {
            SfvChecker checker = new SfvChecker(chooser.getSelectedFile().getAbsolutePath());
            checker.setEventHandler(new SfvCheckerEventHandler() {

                @Override
                public void handle(SfvCheckerEvent event) {
                    System.out.println(event.toString());
                }
            });
            checker.process();
        }
    }
}
