/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.fontchooser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * DefaultFontEditor
 *
 * @depends	/vavi/swing/resource.properties
 *
 * @author	<a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version	0.00	020517	nsano	initial version <br>
 */
public class DefaultFontEditor extends JComponent implements FontEditor {

    /** */
    private static final ResourceBundle rb =
        ResourceBundle.getBundle("vavi.swing.resource", Locale.getDefault());

    /** */
    private JLabel sample;

    private static final int MAX_SIZE = 48;

    private static final String[] styles = {
        rb.getString("jFontChooser.style.plain"),
        rb.getString("jFontChooser.style.bold"),
        rb.getString("jFontChooser.style.italic"),
    };

    private String[] sizes = new String[MAX_SIZE];

    private JList nameList;
    private JList styleList;
    private JList sizeList;

    /** */
    private Font font;

    /**
     * Creates default font editor.
     */
    public DefaultFontEditor() {

        this.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));

        // font
        GraphicsEnvironment ge =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            rb.getString("jFontChooser.style.title.font")));
        nameList = new JList(ge.getAvailableFontFamilyNames());
        nameList.addListSelectionListener(nameListener);
        p.add(new JScrollPane(nameList));
        panel.add(p);

        // style
        p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            rb.getString("jFontChooser.style.title.style")));
        styleList  = new JList(styles);
        styleList.addListSelectionListener(styleListener);
        p.add(new JScrollPane(styleList));
        panel.add(p);

        // size
        for (int i = 1; i <= MAX_SIZE; i++)
            sizes[i-1] = String.valueOf(i);
        p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            rb.getString("jFontChooser.style.title.size")));
        sizeList = new JList(sizes);
        sizeList.addListSelectionListener(sizeListener);
        p.add(new JScrollPane(sizeList));
        panel.add(p);

        this.add(BorderLayout.NORTH, panel);

        // sample
        panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            rb.getString("jFontChooser.title.sample")));
        p = new JPanel();
        p.setBorder(BorderFactory.createLoweredBevelBorder());
        sample = new JLabel(
            "  " + rb.getString("jFontChooser.sample.text") + "  ");
        p.add(sample);
        panel.add(p);
        panel.setPreferredSize(new Dimension(0, 120));
        this.add(BorderLayout.CENTER, panel);
    }

    /** */
    private ListSelectionListener nameListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            String name = (String) nameList.getSelectedValue();
            font = new Font(name, font.getStyle(), font.getSize());
            sample.setFont(font);
        }
    };

    /** */
    private ListSelectionListener styleListener = new ListSelectionListener() {
        final String I = rb.getString("jFontChooser.style.italic");
        final String B = rb.getString("jFontChooser.style.bold");
        public void valueChanged(ListSelectionEvent e) {
            String style = (String) styleList.getSelectedValue();
            int s = I.equalsIgnoreCase(style) ? Font.ITALIC :
                    B.equalsIgnoreCase(style) ? Font.BOLD   :
                                                Font.PLAIN  ;
            font = new Font(font.getName(), s, font.getSize());
            sample.setFont(font);
        }
    };

    /** */
    private ListSelectionListener sizeListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            String size = (String) sizeList.getSelectedValue();
            int i = Integer.parseInt(size);
            font = new Font(font.getName(), font.getStyle(), i);
            sample.setFont(font);
        }
    };

    /** */
    public Component getFontEditorComponent() {
        return this;
    }

    /** */
    public void setSelectedFont(Font font) {
        this.font = font;

        if (font == null)
            return;

        nameList .setSelectedValue(font.getName(), true);
        styleList.setSelectedIndex(font.getStyle());
        sizeList .setSelectedValue(String.valueOf(font.getSize()), true);
    }

    /** */
    public Font getSelectedFont() {
        return font;
    }
}

/* */
