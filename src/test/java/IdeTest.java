/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import vavi.swing.FontChooser;
import vavi.util.Debug;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


/**
 * IdeTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-08-10 nsano initial version <br>
 */
@PropsEntity(url = "file:local.properties")
@EnabledIfSystemProperty(named = "vavi.test", matches = "ide")
public class IdeTest {

    static boolean localPropertiesExists() {
        return Files.exists(Paths.get("local.properties"));
    }

    @Property(name = "foo.bar")
    String dir = "src/test/resources";

    /** using cdl cause junit stops awt thread suddenly */
    CountDownLatch cdl;

    @BeforeEach
    void setup() throws Exception {
        cdl = new CountDownLatch(1);

        if (localPropertiesExists()) {
            PropsEntity.Util.bind(this);
        }
    }

    @AfterEach
    void teardown() throws Exception {
        cdl.await(); // depends on each test frame's exit on close
    }

    @Property(name = "rename.dir")
    String renameDir;

    @Test
    void testFileRenamer() throws Exception {
        FileRenamer.main(new String[] { renameDir });
    }

    @Test
    void testIntersection() throws Exception {
        JFrame frame = new JFrame("Intersection");
        frame.setLocation(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Rectangle r1 = new Rectangle(0, 0, 200, 200);
        Rectangle r2 = new Rectangle(300, 300, 400, 400);
        JPanel panel = new JPanel() {
            @Override
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

    @Test
    void testFontChooser() throws Exception {
        FontChooser fontChooser = new FontChooser(new Font("Dialog", Font.PLAIN, 16));
        fontChooser.showDialog(null);
        Font font = fontChooser.getFont();
Debug.println(font.getFamily() + ", " + font.getName() + ", " + font.getSize() + ", " + font.getStyle());
    }

    @Test
    void period() throws Exception {
        JFrame frame = new JFrame("Period");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel tableModel = new DefaultTableModel(new String[] {"Before", "After"}, 0);
        tableModel.addRow(new Object[] { "sample.ext", "日本語.ext" });
        tableModel.addRow(new Object[] { "aaa.epub", "bbb.pdf" });

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        Font font = table.getFont();

//System.getProperties().forEach((k, v) -> System.err.println(k + ": " + v));
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(320, 480));
        panel.setLayout(new GridLayout(3, 1));
        String text = "<html>" + System.getProperty("java.vendor") + "<br/>" +
                System.getProperty("java.runtime.version") + "</html>";
                panel.add(new JLabel(text));
        panel.add(new JLabel(font.getFamily()));
        JScrollPane sp = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setViewportView(table);
        panel.add(sp);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setSize(320, 480);
//        frame.pack();
        frame.setVisible(true);
        frame.getContentPane().repaint();
        Thread.sleep(10000000);
    }
}
