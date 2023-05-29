/*
 * Copyright (c) 2017 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.util.concurrent.CountDownLatch;
import javax.swing.JFrame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;


/**
 * BorderChooserTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 Nov 15, 2017 umjammer initial version <br>
 */
public class BorderChooserTest {

    @Test
    @EnabledIfSystemProperty(named = "vav.test", matches = "ide")
    public void test() throws Exception {
        main(new String[] {});
        // using cdl cause junit stops awt thread suddenly
        CountDownLatch cdl = new CountDownLatch(1);
        cdl.await(); // depends on main frame's exit on close
    }

    /** */
    public static void main(String[] args) {
        JFrame frame = new JFrame("BorderChooser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new BorderChooser());
        frame.pack();
        frame.setVisible(true);
    }
}

/* */
