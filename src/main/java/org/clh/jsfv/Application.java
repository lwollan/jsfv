package org.clh.jsfv;

import org.clh.jsfv.logging.logger.JPanelEventLogger;
import org.clh.jsfv.logging.logger.SystemOutEventLogger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Application {

    public static void main(String args[]) throws Exception {

        if (args.length == 0 && !GraphicsEnvironment.isHeadless()) {
            gui();
        } else {
            commandLine(args);
        }

    }

    private static void commandLine(String[] args) throws IOException {
        if (args != null && args[0] != null) {
            SfvChecker checker = new SfvChecker(args[0]);
            checker.setEventHandler(new SystemOutEventLogger());
            checker.process();
        } else {
            System.out.println("Missing directory to check.");
            System.exit(-1);
        }
    }

    private static void gui() throws IOException {
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
