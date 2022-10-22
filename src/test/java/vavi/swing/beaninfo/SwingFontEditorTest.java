/*
 * Copyright (c) 2017 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import javax.swing.JFrame;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * SwingFontEditorTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 Nov 15, 2017 umjammer initial version <br>
 */
public class SwingFontEditorTest {

    @Test
    @Disabled
    public void test() {
        fail("Not yet implemented");
    }

    //-------------------------------------------------------------------------

    public static void main(String[] argv) {
        JFrame f = new JFrame("SwingFontEditor");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingFontEditor editor = new SwingFontEditor();
        f.getContentPane().add(editor.getCustomEditor());
        f.pack();
        f.setVisible(true);
    }
}

/* */
