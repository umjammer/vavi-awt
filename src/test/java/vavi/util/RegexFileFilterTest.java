/*
 * Copyright (c) 2012 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * RegexFileFilterTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2012/10/16 umjammer initial version <br>
 */
public class RegexFileFilterTest {

    @Test
    public void test() throws IOException {
        System.err.println(".*\\.xml");
        Path resources = Paths.get("src/test/resources/vavi/util");
        Files.list(resources).filter(new RegexFileFilter(".*\\.xml")).forEach(System.err::println);
        assertEquals(Files.list(resources).filter(new RegexFileFilter(".*\\.xml")).count(), 3);
        System.err.println("regex_file_filter_0[14]\\..+");
        Files.list(resources).filter(new RegexFileFilter("regex_file_filter_0[14]\\..+")).forEach(System.err::println);
        assertEquals(Files.list(resources).filter(new RegexFileFilter("regex_file_filter_0[14]\\..+")).count(), 2);
    }

    /** Tests this class. */
    public static void main(String[] args) {
        RegexFileFilter fileFilter = new RegexFileFilter(".*\\.xml");
        JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
        fc.setFileFilter(fileFilter);
        fc.showOpenDialog(null);
        System.exit(0);
    }
}
