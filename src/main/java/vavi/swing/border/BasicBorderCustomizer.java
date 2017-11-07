/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;

import vavi.swing.propertyeditor.JPropertyEditorTable;


/**
 * Border クラスの Customizer の基底クラスです．
 * 左側にボーダーのサンプル，右側にボーダーのプロパティエディタテーブル の UI
 * で編集します．
 *
 * @done Customizer としては，値が変わるたびに firePropertyChange する
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020523 nsano initial version <br>
 *          1.00 020527 nsano complete <br>
 */
public class BasicBorderCustomizer extends JComponent
    implements BorderCustomizer {

    /** the border instance which the bean has */
    protected Border border;

    /** the displayed sample for border */
    protected JLabel borderSample;

    /** table model for border properties */
    protected BorderPropertyDescriptorTableModel tableModel;

    /** */
    public BasicBorderCustomizer() {

        this.setLayout(new GridLayout(1, 2));

        borderSample = new JLabel();
        borderSample.setText("sample");
        borderSample.setHorizontalAlignment(JLabel.CENTER);
        borderSample.setOpaque(true);
        borderSample.setBackground(UIManager.getColor("Desktop.background"));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(borderSample);

        this.add(panel);

        tableModel = new BorderPropertyDescriptorTableModel();
        JTable table = new JPropertyEditorTable(tableModel);
        JScrollPane sp = new JScrollPane(table);

        this.add(sp);
    }

    /** */
    public void setObject(Border border) {
        Border oldBorder = this.border;
        this.border = border;

        tableModel.setObject(border);
        tableModel.fireTableDataChanged();

        firePropertyChange("border", oldBorder, border);
//Debug.println(this.border);
    }

    /** TODO TabbedPane の ChangeEvent のためだけ */
    public Border getObject() {
//Debug.println(border);
        return border;
    }
}

/* */
