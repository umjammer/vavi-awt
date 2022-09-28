/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import vavi.swing.JHistoryComboBox;
import vavi.util.CharNormalizerJa;
import vavi.util.Debug;
import vavi.util.DiffMatchPatch;


/**
 * FileRenamer.
 * <p>
 * TODO realtime regex colorize
 *      dirty status
 *      duplication check after replace
 *      widget set un-enabled (combo-box is working when un-enabled)
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-08-09 nsano initial version <br>
 */
public class FileRenamer {

    public static void main(String[] args) throws Exception {
        FileRenamer app = new FileRenamer();
        app.dir = Paths.get(args[0]);
        app.gui();
        app.init();
    }

    JHistoryComboBox searcher = new JHistoryComboBox();
    JHistoryComboBox replacer = new JHistoryComboBox();
    JTable table = new JTable();
    JButton executeButton = new JButton("▶ Execute");
    JButton commitButton = new JButton("✓ Commit");
    JButton undoButton = new JButton("⎌ Undo");
    DefaultTableModel model;
    JCheckBox normalize = new JCheckBox("Normalize");

    void gui() {
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1600, 1200));
        panel.setLayout(new BorderLayout());

        JPanel tool = new JPanel();
        tool.setLayout(new GridLayout(3, 2));

        JToolBar bar = new JToolBar();

        bar.add(commitButton);
        bar.add(new JSeparator());
        bar.add(normalize);
        bar.add(undoButton);
        bar.add(executeButton);

        tool.add(searcher);
        tool.add(replacer);
        tool.add(bar);

        panel.add(tool, BorderLayout.NORTH);
        panel.add(table, BorderLayout.CENTER);

        commitButton.addActionListener(this::commitAction);
        executeButton.addActionListener(this::executeAction);
        undoButton.addActionListener(this::undoAction);
        normalize.addChangeListener(ev -> {
            replacer.setEditable(!normalize.isSelected());
        });

        searcher.restoreHistory(FileRenamer.class.getName() + ".searcher");
        replacer.restoreHistory(FileRenamer.class.getName() + ".replacer");

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                searcher.saveHistory(FileRenamer.class.getName() + ".searcher");
                replacer.saveHistory(FileRenamer.class.getName() + ".replacer");
            }
        });

        frame.setContentPane(panel);
        frame.setTitle("File Renamer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /** file list in the working directory */
    private List<Path> sources = new ArrayList<>();

    /** working directory */
    private Path dir;

    /** initialize model */
    private void init() throws IOException {
        sources.clear();
Debug.println(dir + ": " + Files.exists(dir));
        model = new DefaultTableModel(new String[] {"Before", "After"}, 0);
        Files.list(dir).sorted().forEach(path -> {
Debug.println(path.getFileName());
            sources.add(path);
            model.addRow(new Object[] { path.getFileName().toString(), path.getFileName().toString() });
        });
        table.setModel(model);
    }

    /** colors */
    private static class ColorSchema {
        static final Color C0 = Color.black;
        static final Color C1 = Color.red;
        Color[] colors = new Color[] {Color.cyan, Color.yellow, Color.green};
        int index = 0;
        Color next() {
            Color color = colors[index++];
            if (index == colors.length) {
                index = 0;
            }
            return color;
        }
    }

    /** coloerize regex group */
    private static class RegexColorizer {
        char[] colors;
        char c;
        String target;
        RegexColorizer(String target) {
            this.target = target;
            colors = new char[target.length()];
            c = '0';
            Arrays.fill(colors, 0, colors.length, c++);
        }
        /** select partial */
        void update(int start, int end) {
            Arrays.fill(colors, start, end, c++);
        }
        /** get result */
        String colorize() {
            ColorSchema schema = new ColorSchema();
            // https://stackoverflow.com/a/23523874
            String[] parts = new String(colors).split("(?<=(.))(?!\\1)");
            StringBuilder sb = new StringBuilder("<html>");
            int s = 0;
            for (String part : parts) {
                Color c = part.charAt(0) == '0' ? ColorSchema.C0 : part.charAt(0) == '1' ? ColorSchema.C1 : schema.next();
                sb.append(String.format("<span style='color:#%02x%02x%02x'>", c.getRed(), c.getGreen(), c.getBlue()));
                sb.append(target.substring(s, s + part.length()));
                sb.append("</span>");
                s += part.length();
            }
            return sb.toString();
        }
    }

    /** coloerize diff */
    static class DiffColorizer {
        String a, b;
        DiffColorizer(String a, String b) {
            this.a = a;
            this.b = b;
        }
        String colorize() {
            StringBuilder sb = new StringBuilder("<html>");
            for (DiffMatchPatch.Diff diff : new DiffMatchPatch().diff_main(a, b)) {
                Color c;
                switch (diff.operation) {
                case DELETE:
                    c = Color.red;
                    sb.append(String.format("<span style='color:#%02x%02x%02x'>", c.getRed(), c.getGreen(), c.getBlue()));
                    sb.append(diff.text);
                    sb.append("</span>");
                    break;
                case INSERT:
                    c = Color.green;
                    sb.append(String.format("<span style='color:#%02x%02x%02x'>", c.getRed(), c.getGreen(), c.getBlue()));
                    sb.append(diff.text);
                    sb.append("</span>");
                    break;
                case EQUAL:
                    sb.append(diff.text);
                    break;
                }
            }
            return sb.toString();
        }
    }

    /** simulate replacement */
    private void executeAction(ActionEvent event) {
        if (normalize.isSelected()) {
            normalize();
        } else {
            Pattern searcher = Pattern.compile(this.searcher.getText());
            String replacer = this.replacer.getText();
            rename(searcher, replacer);
        }
    }

    /** rename by regex */
    private void rename(Pattern searcher, String replacer) {
Debug.println("rename: " + searcher.pattern() + ", " + replacer);

        for (int i = 0; i < sources.size(); i++) {
            String filename = sources.get(i).getFileName().toString();
            Matcher matcher = searcher.matcher(filename);
            RegexColorizer colorizer = new RegexColorizer(filename);

            while (matcher.find()) {
                String replacing = replacer;
Debug.println(filename + ": found: " + matcher.groupCount() + ", " + matcher.start() + ", " + matcher.end());
                String p1 = filename.substring(0, matcher.start());
                String p2 = filename.substring(matcher.start(), matcher.end());
                String p3 = filename.substring(matcher.end());
                colorizer.update(matcher.start(), matcher.end());
                for (int j = 0; j < matcher.groupCount(); j++) {
Debug.println("group: " + replacing + ", " + matcher.group(j + 1) + ", " + matcher.start(j + 1) + ", " + matcher.end(j + 1));
                    replacing = replacing.replaceFirst("\\$" + (j + 1), matcher.group(j + 1));
                    colorizer.update(matcher.start(j + 1), matcher.end(j + 1));
                }
                model.setValueAt(colorizer.colorize(), i, 0);
                model.setValueAt(p1 + replacing + p3, i, 1);
            }
        }
    }

    /** normalize, 0 padding single digit */
    private void normalize() {
Debug.println("normalize: " + sources.size());

        for (int i = 0; i < sources.size(); i++) {
            // to half
            String filename = sources.get(i).getFileName().toString();
            String replaced = CharNormalizerJa.ToHalfAns2.normalize(filename);

            // 0 padding single digit
            Pattern singleNumber = Pattern.compile("([\\D])(\\d)([\\D]|$)");
            Matcher matcher = singleNumber.matcher(replaced);

            while (matcher.find()) {
                String p1 = replaced.substring(0, matcher.start());
                String p2 = replaced.substring(matcher.start(), matcher.end());
                String p3 = replaced.substring(matcher.end());

                if (matcher.groupCount() == 3) {
                    replaced = p1 + matcher.group(1) + 0 + matcher.group(2) + matcher.group(3) + p3;
                } else {
Debug.println("groups: " + matcher.groupCount());
                }
            }

            // reflection
            if (!filename.equals(replaced)) {
                DiffColorizer colorizer = new DiffColorizer(filename, replaced);
                model.setValueAt(colorizer.colorize(), i, 0);

                model.setValueAt(replaced, i, 1);
            }
        }
    }

    /** process real files */
    void commitAction(ActionEvent event) {
        for (int i = 0; i < sources.size(); i++) {
            String filename = sources.get(i).getFileName().toString();
            String replaceWith = (String) model.getValueAt(i, 1);

            if (!filename.equals(replaceWith)) {
Debug.println("rename: " + filename + " -> " + replaceWith);
                try {
                    Files.move(sources.get(i), sources.get(i).getParent().resolve(replaceWith));
                } catch (IOException e) {
                    Debug.println(Level.WARNING, e);
                }
            }
        }
        try {
            init();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** */
    void undoAction(ActionEvent event) {
        for (int i = 0; i < sources.size(); i++) {
            String filename = sources.get(i).getFileName().toString();
            model.setValueAt(filename, i, 0);
            model.setValueAt(filename, i, 1);
        }
    }
}
