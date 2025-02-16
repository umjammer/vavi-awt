/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import vavi.swing.JHistoryComboBox;
import vavi.util.Debug;


/**
 * RegexTester.
 * <p>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-20 nsano initial version <br>
 */
public class RegexTester {

    public static void main(String[] args) throws Exception {
        RegexTester app = new RegexTester();
        app.gui();
        app.init();
    }

    final JHistoryComboBox searcher = new JHistoryComboBox();
    final JTable table = new JTable();
    final JButton executeButton = new JButton("▶ Execute");
    final JButton undoButton = new JButton("⎌ Undo");
    DefaultTableModel model;

    void gui() {
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1600, 1200));
        panel.setLayout(new BorderLayout());

        JPanel tool = new JPanel();
        tool.setLayout(new GridLayout(3, 2));

        JToolBar bar = new JToolBar();

        bar.add(new JSeparator());
        bar.add(undoButton);
        bar.add(executeButton);

        tool.add(searcher);
        tool.add(bar);

        panel.add(tool, BorderLayout.NORTH);
        panel.add(table, BorderLayout.CENTER);

        executeButton.addActionListener(this::executeAction);
        undoButton.addActionListener(this::undoAction);

        searcher.restoreHistory(RegexTester.class.getName() + ".searcher");

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                searcher.saveHistory(RegexTester.class.getName() + ".searcher");
            }
        });

        frame.setContentPane(panel);
        frame.setTitle("Regex Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /** file list in the working directory */
    private final List<String> sources = new ArrayList<>();

    /** initialize model */
    private void init() throws IOException {
        sources.clear();
        model = new DefaultTableModel(new String[] {"Before", "After"}, 0);
        Scanner s = new Scanner(RegexTester.class.getResourceAsStream("/web.txt"));
        while (s.hasNextLine()) {
            String l = s.nextLine();
Debug.println(l);
            sources.add(l);
            model.addRow(new Object[] { l, l });
        }
        table.setModel(model);
    }

    /** simulate replacement */
    private void executeAction(ActionEvent event) {
        Pattern searcher = Pattern.compile(this.searcher.getText());
        execute(searcher);
    }

    /** execute regex */
    private void execute(Pattern searcher) {
Debug.println("execute: " + searcher.pattern());

        for (int i = 0; i < sources.size(); i++) {
            // to half
            String target = sources.get(i);
            String replaced = target;

            // 0 padding single digit
            Matcher matcher = searcher.matcher(target);

            while (matcher.find()) {
                String p1 = target.substring(0, matcher.start());
                String p2 = target.substring(matcher.start(), matcher.end());
                String p3 = target.substring(matcher.end());

                if (matcher.groupCount() == 3) {
                    replaced = p1 + matcher.group(1) + 0 + matcher.group(2) + matcher.group(3) + p3;
                } else {
Debug.println("groups: " + matcher.groupCount());
                }
            }

            // reflection
            if (!target.equals(replaced)) {
                FileRenamer.DiffColorizer colorizer = new FileRenamer.DiffColorizer(target, replaced);
                model.setValueAt(colorizer.colorize(), i, 0);

                model.setValueAt(replaced, i, 1);
            }
        }
    }

    /** */
    void undoAction(ActionEvent event) {
        for (int i = 0; i < sources.size(); i++) {
            String target = sources.get(i);
            model.setValueAt(target, i, 0);
            model.setValueAt(target, i, 1);
        }
    }
}
