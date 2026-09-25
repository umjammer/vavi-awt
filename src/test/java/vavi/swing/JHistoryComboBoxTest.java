/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.BorderLayout;
import java.util.prefs.Preferences;
import javax.swing.JFrame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * JHistoryComboBoxTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-08-26 nsano initial version <br>
 */
class JHistoryComboBoxTest {

    @Test
    @EnabledIfSystemProperty(named = "vav.test", matches = "ide")
    public void test() {
        main(new String[] {});
        while (true) Thread.yield();
    }

    @Test
    void testSaveRestore() throws Exception {
        String id = JHistoryComboBoxTest.class.getName() + ".testSaveRestore";
        try {
            JHistoryComboBox cb = new JHistoryComboBox(false);
            cb.addItem("a");
            cb.addItem("b");
            cb.addItem("c");
            cb.addItem("d");
            cb.addItem("b"); // duplication
            cb.setSelectedItem("c");
            assertTrue(cb.removeHistory("d"));
            assertFalse(cb.removeHistory("x"));
            cb.saveHistory(id);

            for (int n = 0; n < 2; n++) { // restoring twice doesn't duplicate
                JHistoryComboBox cb2 = new JHistoryComboBox(false);
                cb2.restoreHistory(id);
                cb2.restoreHistory(id);
                assertEquals(3, cb2.getItemCount());
                assertEquals("c", cb2.getItemAt(0)); // selected one comes first
                assertEquals("a", cb2.getItemAt(1));
                assertEquals("b", cb2.getItemAt(2));
                assertEquals("c", cb2.getSelectedItem());
                cb2.saveHistory(id);
            }
        } finally {
            Preferences.userRoot().node(id).removeNode();
        }
    }

    //----

    /** */
    public static void main(String[] args) {
        JFrame frame = new JFrame("JHistoryComboBox Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.getContentPane().setLayout(new BorderLayout());
        JHistoryComboBox cb = new JHistoryComboBox();
//      cb.setPreferredSize(new Dimension(120, 100));
        frame.getContentPane().add(BorderLayout.NORTH, cb);
        frame.setVisible(true);
    }
}
