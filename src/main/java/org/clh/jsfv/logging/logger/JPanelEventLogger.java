package org.clh.jsfv.logging.logger;

import org.clh.jsfv.logging.Event;

import javax.swing.*;

public class JPanelEventLogger implements EventLogger {

    private final JTextField textField;

    public JPanelEventLogger() {
        JFrame frame = new JFrame();
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        JPanel panel = new JPanel();

        frame.getContentPane().add(panel);
        textField = new JTextField();
        panel.add(textField);
    }

    @Override
    public void log(Event event) {
        textField.setText(event.toString());
        textField.updateUI();
    }
}
