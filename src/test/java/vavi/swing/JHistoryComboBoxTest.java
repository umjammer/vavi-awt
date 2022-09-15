/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.BorderLayout;
import javax.swing.JFrame;


/**
 * JHistoryComboBoxTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-08-26 nsano initial version <br>
 */
class JHistoryComboBoxTest {

    //----

    /** */
    public static void main(String[] args) {
        JFrame frame = new JFrame("JHistiryComboBox Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.getContentPane().setLayout(new BorderLayout());
        JHistoryComboBox cb = new JHistoryComboBox();
//      cb.setPreferredSize(new Dimension(120, 100));
        frame.getContentPane().add(BorderLayout.NORTH, cb);
        frame.setVisible(true);
    }
}
