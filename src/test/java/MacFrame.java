/*
 * Copyright (c) 2009 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;


/**
 * MacFrame. mac frame
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (vavi)
 * @version 0.00 090619 nsano initial version <br>
 */
public class MacFrame {

    /** */
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setLocation(300, 300);
        frame.setAlwaysOnTop(true);
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//if (System.getProperty("java.vendor").startsWith("Apple Inc.")) {
//frame.getRootPane().putClientProperty("Window.alpha", new Float(0.7f)); // Mac specific (MRJ)
//}
        frame.setOpacity(0.7f);
        frame.getContentPane().setLayout(new BorderLayout());
        JLabel label = new JLabel("さの");
        label.setBorder(BorderFactory.createLineBorder(Color.red, 1));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}

/* */
