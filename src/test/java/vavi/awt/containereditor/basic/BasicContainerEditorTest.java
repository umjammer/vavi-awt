/*
 * Copyright (c) 2017 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.containereditor.basic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import vavi.awt.containereditor.ContainerEditor;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * BasicContainerEditorTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 Nov 15, 2017 umjammer initial version <br>
 */
@Disabled
public class BasicContainerEditorTest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    // -------------------------------------------------------------------------

    /** Tests this class. */
    public static void main(String[] args) {
        JFrame f = new JFrame("BasicContainerEditor");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setLayout(new BorderLayout());
        // ((JComponent) f.getContentPane()).setOpaque(true);
        Color bc = UIManager.getColor("Desktop.background");
        f.getContentPane().setBackground(bc);
        // f.getContentPane().addComponentListener(new ComponentAdapter() {
        // public void componentResized(ComponentEvent ev) {
        // Debug.println(ev.getComponent().getSize());
        // }
        // });

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(320, 200));
        panel.setBackground(Color.pink);

        f.getContentPane().add(panel);
        f.pack();

        ContainerEditor editor = new BasicContainerEditor(panel);
        editor.setEditable(true);

        for (int i = 0; i < 6; i++) {
            JPanel cp = new JPanel();

            int r = (int) (Math.random() * 256 % 256);
            int g = (int) (Math.random() * 256 % 256);
            int b = (int) (Math.random() * 256 % 256);

            int x = (int) (Math.random() * 240 % 240);
            int y = (int) (Math.random() * 170 % 170);

            cp.setSize(new Dimension(80, 30));
            cp.setBackground(new Color(r, g, b));
            // Debug.println(x + ", " + y);
            cp.setLocation(new Point(x, y));
            cp.setName("Panel_" + i);

            panel.add(cp, -1);
            // Component cc = new GlassController(cp);
            // panel.add(cc, 0);
        }

        f.setVisible(true);
    }
}

/* */
