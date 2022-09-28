/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.GridBagLayout;

import javax.swing.JFrame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;


/**
 * LayoutManagerChooserTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2021/12/12 umjammer initial version <br>
 */
class LayoutManagerChooserTest {

    @Test
    @EnabledIfSystemProperty(named = "vav.test", matches = "ide")
    public void test() {
        main(new String[] {});
        while (true) Thread.yield();
    }

    // ----

    /** Tests this class. */
    public static void main(String[] args) {
        JFrame frame = new JFrame("LayoutManagerChooser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new LayoutManagerChooser());
        frame.pack();
        frame.setVisible(true);
    }
}

/* */
