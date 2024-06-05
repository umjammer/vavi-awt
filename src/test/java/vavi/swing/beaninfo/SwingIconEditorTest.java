/*
 * Copyright (c) 2017 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * SwingIconEditorTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 Nov 15, 2017 umjammer initial version <br>
 */
public class SwingIconEditorTest {

    @Test
    @Disabled
    public void test() {
        fail("Not yet implemented");
    }

    //-------------------------------------------------------------------------

    public static void main(String[] args) {
        JFrame f = new JFrame("SwingIconEditor");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final SwingIconEditor editor = new SwingIconEditor();
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(editor.getCustomEditor(), BorderLayout.NORTH);
        final JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(320, 240));
        label.setOpaque(true);
        label.setBackground(Color.pink);
        label.setHorizontalAlignment(JLabel.CENTER);
        f.getContentPane().add(label);
        editor.addPropertyChangeListener(ev -> label.setIcon((Icon) editor.getValue()));

        f.pack();
        f.setVisible(true);
    }
}
