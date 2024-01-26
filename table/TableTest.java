/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding.table;

import java.awt.image.BufferedImage;

import org.w3c.dom.Document;


/**
 * TableTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022/11/26 nsano initial version <br>
 */
class TableTest {

    @Table(row = TestTable.TestRow.class, iterable = "entries")
    public static class TestTable {

        @Row(setter = "setEntry")
        public static class TestRow {
            Document document;
            public void setEntry(Document document) {
                this.document = document;
            }
            @Column(sequence = 1)
            public String getName() {
                return null;
            }
            @Column(sequence = 2)
            public BufferedImage getImage() {
                return null;
            }
        }
    }
}