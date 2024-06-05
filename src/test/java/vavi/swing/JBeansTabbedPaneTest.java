/*
 * Copyright (c) 2018 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;


/**
 * JBeansTabbedPaneTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2018/03/07 umjammer initial version <br>
 */
public class JBeansTabbedPaneTest {

    @Test
    @EnabledIfSystemProperty(named = "vav.test", matches = "ide")
    public void test() {
        main(new String[] {});
        while (true) Thread.yield();
    }

    //-------------------------------------------------------------------------

    /** */
    public static void main(String[] args) {
        JFrame frame = new JFrame("JBeansTabbedPane Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JBeansTabbedPane t = new JBeansTabbedPane();
        t.setPreferredSize(new Dimension(640, 80));
        frame.getContentPane().add(t);
        frame.pack();
        frame.setVisible(true);
    }
}
