package org.clh.jsfv;

import org.clh.jsfv.logging.JPanelEventLogger;
import org.clh.jsfv.logging.SystemOutEventLogger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Application {

    public static void main(String args[]) throws Exception {

        if (args.length == 0 && !GraphicsEnvironment.isHeadless()) {
            GUI();
        } else {
            CommandLine(args[0]);
        }

    }

    private static void CommandLine(String arg) throws IOException {
        SfvChecker checker = new SfvChecker(arg);
        checker.setEventHandler(new SystemOutEventLogger());
        checker.process();
    }

    private static void GUI() throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int openDialog = chooser.showOpenDialog(null);
        if (openDialog == JFileChooser.APPROVE_OPTION) {
            SfvChecker checker = new SfvChecker(chooser.getSelectedFile().getAbsolutePath());
            checker.setEventHandler(new JPanelEventLogger());
            checker.process();
        } else {
            System.exit(-1);
        }
    }

}
