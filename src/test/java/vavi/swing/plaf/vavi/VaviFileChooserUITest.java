/*
 * Copyright (c) 2018 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.plaf.vavi;

import java.io.File;

import javax.swing.JFileChooser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * VaviFileChooserUITest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2018/02/18 umjammer initial version <br>
 */
@Disabled
public class VaviFileChooserUITest {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    /** */
    public static void main(String[] args) {
        JFileChooser fc = new JFileChooser();
        System.err.println(fc.getUI().getClass());
        fc.setSelectedFile(new File("T400.java"));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.showOpenDialog(null);
        System.err.println(fc.getSelectedFile());
        System.exit(0);
    }
}

/* */
