/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;


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
            VO data;
            public void setEntry(VO data) {
                this.data = data;
            }
            @Column(sequence = 0)
            public String getName() {
                return data.name;
            }
            @Column(sequence = 1)
            public String getType() {
                return data.type;
            }
            @Column(sequence = 2)
            public String getPilot() {
                return data.pilot;
            }
        }

        public static class VO {
            String type;
            String name;
            String pilot;
            VO(String type, String name, String pilot) {
                this.type = type;
                this.name = name;
                this.pilot = pilot;
            }
        }

        void addRow(String type, String name, String pilot) {
            VO vo = new VO(type, name, pilot);
            rows.add(vo);
        }

        List<VO> rows = new ArrayList<>();

        public Iterable<VO> entries() {
            return rows;
        };
    }

    @Test
    @EnabledIfSystemProperty(named = "vavi.test", matches = "ide")
    void test1() throws Exception {
        JFrame frame = new JFrame();
        frame.setTitle("Mobile Suit");

        JTable table = new JTable();
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.getTableHeader().setReorderingAllowed(false);

        TestTable model = new TestTable();
        model.addRow("MS-06", "ZAKU", "Gene");
        model.addRow("MS-06S", "Char's ZAKU", "Char Aznable");
        model.addRow("MS-07", "GOUF", "Ramba Ral");
        model.addRow("MS-09", "DOM", "Gaia");
        model.addRow("MSM-03", "GOGG", "Koka Lasa");
        model.addRow("MSM-07", "Z'GOK", "Callahan");
        model.addRow("MSM-04", "ACGUY", "Akahana");
        model.addRow("MSM-10", "ZOCK", "Bolasquniph");
        model.addRow("MS-09R", "RICK DOM", "Francy");
        model.addRow("YMS-14", "GELGOOG", "Char Aznable");
        model.addRow("YMS-15", "GYAN", "M'quve");
        model.addRow("MSN-02", "ZEONG", "Char Aznable");
        TableModel<?> tableModel = new TableModel<>(model);
        tableModel.bind(table);

        JScrollPane sp = new JScrollPane(table);

        CountDownLatch cdl = new CountDownLatch(1);

        frame.addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) { cdl.countDown(); }});
        frame.getContentPane().add(sp, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(640, 800));
        frame.pack();
        frame.setVisible(true);

        cdl.await();
    }
}