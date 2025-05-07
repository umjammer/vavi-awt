/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;

import vavi.swing.propertyeditor.JPropertyEditorTable;

import static java.lang.System.getLogger;


/**
 * This is the base class for Customizer of the Border class.
 * On the left is a border sample, and on the right is the border property editor table, with editing UI.
 *
 * @done As a Customizer, firePropertyChange every time the value changes.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020523 nsano initial version <br>
 *          1.00 020527 nsano complete <br>
 */
public class BasicBorderCustomizer extends JComponent
    implements BorderCustomizer {

    private static final Logger logger = getLogger(BasicBorderCustomizer.class.getName());

    /** the border instance which the bean has */
    protected Border border;

    /** the displayed sample for border */
    protected final JLabel borderSample;

    /** table model for border properties */
    protected final BorderPropertyDescriptorTableModel tableModel;

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

    @Override
    public void setObject(Border border) {
        Border oldBorder = this.border;
        this.border = border;

        tableModel.setObject(border);
        tableModel.fireTableDataChanged();

        firePropertyChange("border", oldBorder, border);
logger.log(Level.TRACE, this.border);
    }

    /** TODO Only for TabbedPane's ChangeEvent */
    @Override
    public Border getObject() {
logger.log(Level.TRACE, border);
        return border;
    }
}
