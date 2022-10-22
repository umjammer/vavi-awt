/*
 * Copyright (c) 2017 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.beaninfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * SwingLayoutManagerEditorTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 Nov 15, 2017 umjammer initial version <br>
 */
public class SwingLayoutManagerEditorTest {

    @Test
    @Disabled
    public void test() {
        fail("Not yet implemented");
    }

    //-------------------------------------------------------------------------

    public static void main(String[] args) {
        JFrame f = new JFrame("SwingLayoutManagerEditor");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final SwingLayoutManagerEditor editor = new SwingLayoutManagerEditor();
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(editor.getCustomEditor(), BorderLayout.NORTH);
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(100, 80));
        panel.setBackground(Color.pink);
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(panel);
        f.getContentPane().add(p);
        for (int i = 0; i < 6; i++) {
            JPanel cp = new JPanel();
            int r  = (int) (Math.random() * 256 % 256);
            int g  = (int) (Math.random() * 256 % 256);
            int b  = (int) (Math.random() * 256 % 256);
//Debug.println(r + ", " + g + ", " + b);
            cp.setBackground(new Color(r, g, b));
            panel.add(cp);
        }
        editor.setValue(panel.getLayout());
        editor.setBean(panel);
        editor.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
//Debug.println(editor.getValue());
                panel.setLayout((LayoutManager) editor.getValue());
            }
        });

        f.pack();
        f.setVisible(true);
    }
}

/* */
