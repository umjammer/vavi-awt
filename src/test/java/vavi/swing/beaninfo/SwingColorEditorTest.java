/*
 * Copyright (c) 2017 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * SwingColorEditorTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 Nov 15, 2017 umjammer initial version <br>
 */
@Disabled
public class SwingColorEditorTest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    //-------------------------------------------------------------------------

    public static void main(String[] argv) {
        JFrame f = new JFrame("SwingColorEditor");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final SwingColorEditor editor = new SwingColorEditor();
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(editor.getCustomEditor(), BorderLayout.NORTH);
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(100, 80));
        panel.setBackground(Color.pink);
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(panel);
        f.getContentPane().add(p);
        editor.setValue(panel.getBorder());
        editor.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
//Debug.println(ev.getOldValue() + ", " + ev.getNewValue());
                panel.setBackground((Color) editor.getValue());
            }
        });

        f.pack();
        f.setVisible(true);
    }
}

/* */
