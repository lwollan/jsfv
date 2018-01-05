package org.clh.jsfv.logging.logger;

import org.clh.jsfv.logging.Event;

import javax.swing.*;

public class JPanelEventLogger implements EventLogger {

    private final JTextField textField;
    private JFrame frame;

    public JPanelEventLogger() {
        frame = new JFrame();
        frame.setSize(600, 100);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        JPanel panel = new JPanel();

        frame.getContentPane().add(panel);
        textField = new JTextField();
        textField.setSize(580, 80);
        textField.setVisible(true);
        panel.add(textField);
    }

    @Override
    public void log(Event event) {
        textField.setText(event.toString());
        frame.getGraphics().dispose();
    }
}
