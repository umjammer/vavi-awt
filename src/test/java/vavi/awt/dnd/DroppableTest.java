/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.dnd;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import vavi.util.Debug;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;


/**
 * DroppableTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2026-09-25 nsano initial version <br>
 */
class DroppableTest {

    JTextArea textArea = new JTextArea();

    @Test
    @EnabledIfSystemProperty(named = "vavi.test", matches = "ide")
    void test1() throws Exception {
        textArea.setPreferredSize(new Dimension(640, 480));
        Droppable.makeComponentMultiplePathDroppable(textArea, this::dropped);
        JFrame frame = new JFrame();
        CountDownLatch cdl = new CountDownLatch(1);
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { cdl.countDown(); }
        });
        frame.getContentPane().add(textArea);
        frame.pack();
        frame.setVisible(true);
        cdl.await();
Debug.print("DONE");
        frame.setVisible(false);
        frame.dispose();
    }

    boolean dropped(List<Path> paths) {
        textArea.setText(null);
        textArea.append(String.join("\n", paths.stream().map(Path::toString).toList()));
        return true;
    }
}
