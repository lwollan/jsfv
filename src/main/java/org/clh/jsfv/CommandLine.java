package org.clh.jsfv;

import org.clh.jsfv.file.SfvChecker;
import org.clh.jsfv.logging.SystemOutEventLogger;

import javax.swing.*;

public class CommandLine {

    public static void main(String args[]) throws Exception {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int openDialog = chooser.showOpenDialog(null);
        if (openDialog == JFileChooser.APPROVE_OPTION) {
            SfvChecker checker = new SfvChecker(chooser.getSelectedFile().getAbsolutePath());
            checker.setEventHandler(new SystemOutEventLogger());
            checker.process();
        }
    }

}
