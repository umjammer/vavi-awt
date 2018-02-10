/*
 * Copyright (c) 2017 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import javax.swing.JFrame;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.fail;


/**
 * BorderChooserTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 Nov 15, 2017 umjammer initial version <br>
 */
@Ignore
public class BorderChooserTest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    //-------------------------------------------------------------------------

    /** Tests this class. */
    public static void main(String[] args) {
        JFrame frame = new JFrame("BorderChooser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new BorderChooser());
        frame.pack();
        frame.setVisible(true);
    }
}

/* */
