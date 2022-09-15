/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;


/**
 * IdeTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-08-10 nsano initial version <br>
 */
public class IdeTest {

    @Test
    @EnabledIfSystemProperty(named = "vavi.test", matches = "ide")
    void testFileRenamer() throws Exception {
        FileRenamer.main(new String[] { System.getProperty("user.home") + "/Downloads/JDownloader/wip3" });
        while (true)
            Thread.yield();
    }

    @Test
    @EnabledIfSystemProperty(named = "vavi.test", matches = "ide")
    void testIntersection() throws Exception {
        JFrame frame = new JFrame();
        frame.setLocation(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Rectangle r1 = new Rectangle(0, 0, 200, 200);
        Rectangle r2 = new Rectangle(300, 300, 400, 400);
        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(Color.red);
                g.fillRect(r1.x, r1.y, r1.width, r1.height);

                g.setColor(Color.blue);
                g.fillRect(r2.x, r2.y, r2.width, r2.height);

                if (r1.intersects(r2)) {
                    g.setColor(Color.magenta);
                    Rectangle r = r1.intersection(r2);
                    g.fillRect(r.x, r.y, r.width, r.height);
                }
            }
        };
        panel.setPreferredSize(new Dimension(1200, 1200));

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        for (int i = 0; i < panel.getWidth() / 10; i ++) {
            r1.x += 10;
            r1.y += 10;
            Thread.sleep(100);
            panel.repaint();
        }
    }
}
