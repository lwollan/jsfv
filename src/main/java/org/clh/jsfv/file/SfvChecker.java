package org.clh.jsfv.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
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
        File[] sfvFiles = listFilesAsArray(file, filter , true);
        for (File file : sfvFiles) {
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
    }
    
    public static File[] listFilesAsArray(
            File directory,
            FilenameFilter filter,
            boolean recurse)
    {
        Collection<File> files = listFiles(directory,
                filter, recurse);
    //Java4: Collection files = listFiles(directory, filter, recurse);
        
        File[] arr = new File[files.size()];
        return files.toArray(arr);
    }

    public static Collection<File> listFiles(
    // Java4: public static Collection listFiles(
            File directory,
            FilenameFilter filter,
            boolean recurse)
    {
        // List of files / directories
        Vector<File> files = new Vector<File>();
    // Java4: Vector files = new Vector();
        
        // Get files / directories in the directory
        File[] entries = directory.listFiles();
        
        // Go over entries
        for (File entry : entries)
        {

            // If there is no filter or the filter accepts the 
            // file / directory, add it to the list
            if (filter == null || filter.accept(directory, entry.getName()))
            {
                files.add(entry);
            }
            
            // If the file is a directory and the recurse flag
            // is set, recurse into the directory
            if (recurse && entry.isDirectory())
            {
                files.addAll(listFiles(entry, filter, recurse));
            }
        }
        
        // Return collection of files
        return files;       
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
