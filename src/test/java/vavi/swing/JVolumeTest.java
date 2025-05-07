/*
 * Copyright (c) 2025 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.Dimension;
import javax.swing.JFrame;


/**
 * JVolumeTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2025-05-07 nsano initial version <br>
 */
class JVolumeTest {

    /** */
    public static void main(String[] args) {
        JFrame frame = new JFrame("JVolume Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JVolume volume = new JVolume();
        volume.setPreferredSize(new Dimension(120, 100));
        frame.getContentPane().add(volume);
        frame.pack();
        frame.setVisible(true);

//        Enumeration e = UIManager.getDefaults().keys();
//        while (e.hasMoreElements()) {
//            Object o = e.nextElement();
//            System.err.println(o + "=" + UIManager.getDefaults().get(o));
//        }
    }
}
