/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


/**
 * JTreeComboBox のデフォルトのセルレンダラです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020330 nsano initial version <br>
 */
public class DefaultTreeComboBoxCellRenderer<E> extends JLabel implements ListCellRenderer<E> {

    /** */
    static final int OFFSET = 16;

    /** */
    static Border emptyBorder = new EmptyBorder(0, 0, 0, 0);

    Icon leafIcon = UIManager.getIcon("Tree.closedIcon");

    Icon nodeIcon = UIManager.getIcon("Tree.leafIcon");

    // Icon nodeIcon = UIManager.getIcon("Tree.openIcon");

    /** */
    public DefaultTreeComboBoxCellRenderer() {
        setOpaque(true);
    }

    /** */
    public Component getListCellRendererComponent(JList<? extends E> listbox, E value, int index, boolean isSelected, boolean cellHasFocus) {

        ListEntry listEntry = (ListEntry) value;
        if (listEntry != null) {
            Border border;
            setText(listEntry.object().toString());
            setIcon(listEntry.isNode() ? nodeIcon : leafIcon);
            if (index != -1)
                border = new EmptyBorder(0, OFFSET * listEntry.level(), 0, 0);
            else
                border = emptyBorder;

            if (UIManager.getLookAndFeel().getName().equals("CDE/Motif")) {
                if (index == -1)
                    setOpaque(false);
                else
                    setOpaque(true);
            } else
                setOpaque(true);

            setBorder(border);
            if (isSelected) {
                setBackground(UIManager.getColor("ComboBox.selectionBackground"));
                setForeground(UIManager.getColor("ComboBox.selectionForeground"));
            } else {
                setBackground(UIManager.getColor("ComboBox.background"));
                setForeground(UIManager.getColor("ComboBox.foreground"));
            }
        } else {
            setText("");
        }

        return this;
    }
}

/* */
