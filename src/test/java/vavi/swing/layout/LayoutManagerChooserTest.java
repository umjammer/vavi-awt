/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
    public void test() throws Exception {
        main(new String[] {});
        // using cdl cause junit stops awt thread suddenly
        CountDownLatch cdl = new CountDownLatch(1);
        cdl.await(); // depends on main frame's exit on close
    }

    /** */
    public static void main(String[] args) {
        JFrame frame = new JFrame("LayoutManagerChooser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LayoutManagerChooser lmc = new LayoutManagerChooser();
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(600, 600));
        String[] nn = new String[] {"A", "B", "C", "D"};
        Arrays.stream(nn).forEach(n -> {
            JButton b = new JButton(n);
            b.setPreferredSize(new Dimension(40, 20));
            p.add(b);
        });
        lmc.setSelectedContainer(p);
        frame.getContentPane().add(lmc);
        frame.pack();
        frame.setVisible(true);
    }
}

/* */
