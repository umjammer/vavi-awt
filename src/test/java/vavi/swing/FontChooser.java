
package vavi.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class FontChooser {
    private Font font;

    private JPanel panel;
    private JComboBox<?> family;
    private JComboBox<String> style;
    private JComboBox<?> size;
    private JLabel example;
    private JDialog dialog;

    private final static String[] styles = {
        "Plain", "Bold", "Italic", "Bold Italic"
    };

    private final static int[] styleCodes = {
        Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC
    };

    public final static int APPROVE_OPTION = 0;
    public final static int CANCEL_OPTION = 1;

    public FontChooser(Font fnt) {
        font = new Font(fnt.getFamily(), fnt.getStyle(), fnt.getSize());

        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new FlowLayout());

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = env.getAllFonts();
        Vector<String> families = new Vector<>();
        for (int i = 0; i < fonts.length; i++) {
            String f = fonts[i].getFamily();
            if (families.indexOf(f) == -1) {
                families.add(fonts[i].getFamily());
            }
        }
        family = new JComboBox<>(families);
        family.setSelectedItem(font.getFamily());
        family.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    String f = (String) event.getItem();
                    font = new Font(f, font.getStyle(), font.getSize());
                    example.setFont(font);

                    if (dialog != null) {
                        dialog.pack();
                    }
                }
            }
        });

        style = new JComboBox<>(styles);
        for (int i = 0; i < styleCodes.length; i++) {
            if (styleCodes[i] == font.getStyle()) {
                style.setSelectedItem(styles[i]);
                break;
            }
        }
        style.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    String s = (String) event.getItem();
                    int sc = Font.PLAIN;
                    for (int i = 0; i < styles.length; i++) {
                        if (s.equals(styles[i])) {
                            sc = styleCodes[i];
                            break;
                        }
                    }

                    font = new Font(font.getFamily(), sc, font.getSize());
                    example.setFont(font);

                    if (dialog != null) {
                        dialog.pack();
                    }
                }
            }
        });

        Vector<String> sizes = new Vector<>();
        for (int i = 4; i < 90; i += 4) {
            sizes.add(Integer.toString(i));
        }
        size = new JComboBox<>(sizes);
        size.setSelectedItem(Integer.toString(font.getSize()));
        size.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    String s = (String) event.getItem();
                    font = new Font(font.getFamily(), font.getStyle(), Integer.parseInt(s));
                    example.setFont(font);

                    if (dialog != null) {
                        dialog.pack();
                    }
                }
            }
        });

        selectPanel.add(family);
        selectPanel.add(style);
        selectPanel.add(size);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        example = new JLabel("Computer is Network. 0123456789");
        example.setFont(font);

        panel.add(selectPanel, BorderLayout.CENTER);
        panel.add(example, BorderLayout.SOUTH);
    }

    public int showDialog(Component comp, Font font) {
        font = new Font(font.getFamily(), font.getStyle(), font.getSize());
        example.setFont(font);
        return showDialog(comp);
    }

    public int showDialog(Component comp) {
        JOptionPane pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null, null);
        dialog = pane.createDialog(comp, "Font Chooser");
        dialog.setVisible(true);

        Integer result = null;
        while (true) {
            Object obj = pane.getValue();
            if (obj instanceof Integer) {
                result = (Integer) obj;
                break;
            } else {
                dialog.setVisible(true);
            }
        }
        dialog = null;
        if (result.intValue() == JOptionPane.OK_OPTION) {
            return APPROVE_OPTION;
        } else {
            return CANCEL_OPTION;
        }
    }

    public Font getFont() {
        return font;
    }
}
