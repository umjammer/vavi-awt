/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.propertyeditor;

import java.awt.Component;
import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


/**
 * Column model for the PropertyTable
 *
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 1.50 000503 original version <br>
 *          1.51 020523 nsano add border, layoutManager <br>
 */
public class PropertyColumnModel extends DefaultTableColumnModel  {

    /** Resource Bundles */
    private static final ResourceBundle rb =
        ResourceBundle.getBundle("vavi.swing.resource", Locale.getDefault());

    private final static String COL_LABEL_PROP = rb.getString("common.label.property.text");

//  private final static String COL_LABEL_DESC = rb.getString("common.label.description.text");

    private final static String COL_LABEL_VALUE = rb.getString("common.label.value.text");

    private static final int minColWidth = 150;

    /**
     */
    public PropertyColumnModel()  {
        // Configure the columns and add them to the model
        TableColumn column;

        // Property
        column = new TableColumn(0);
        column.setHeaderValue(COL_LABEL_PROP);
        column.setPreferredWidth(minColWidth);
        column.setCellRenderer(nameRenderer);
        addColumn(column);

        // Value
        column = new TableColumn(1);
        column.setHeaderValue(COL_LABEL_VALUE);
        column.setPreferredWidth(minColWidth * 2);
        column.setCellEditor(new PropertyValueEditor());
        column.setCellRenderer(valueRenderer);
        addColumn(column);
    }

    /**
     * Renders the name of the property. Sets the short description of the
     * property as the tooltip text.
     */
    private final TableCellRenderer nameRenderer = new DefaultTableCellRenderer() {

        /**
         * Get UI for current editor, including custom editor button
         * if applicable.
         */
        @Override
        public Component getTableCellRendererComponent(JTable jtable,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {

            JPropertyEditorTable table = (JPropertyEditorTable) jtable;

            setToolTipText(table.getShortDescription(row));
            setBackground(UIManager.getColor("control"));
            // blur uneditable
            if (table.isCellEditable(row, JPropertyEditorTable.COL_VALUE)) {
                setForeground(UIManager.getColor("textText"));
            } else {
                setForeground(UIManager.getColor("controlShadow"));
            }
            return super.getTableCellRendererComponent(table,
                                                       value,
                                                       isSelected,
                                                       hasFocus,
                                                       row,
                                                       column);
        }
    };

    /**
     * Renderer for a value with a property editor or installs the default
     * cell renderer.
     */
    private final TableCellRenderer valueRenderer = new DefaultTableCellRenderer() {

        private final Map<Class<?>, PropertyEditor> editors = new HashMap<>();

        private PropertyEditor editor;

        private final Border selectedBorder =
            BorderFactory.createLineBorder(
                UIManager.getColor("Table.selectionBackground"), 1);
        private final Border emptyBorder =
            BorderFactory.createEmptyBorder(1, 1, 1, 1);

        /**
         * Get UI for current editor, including custom editor button
         * if applicable.
         * XXX - yuck! yuck! yuck!!!!
         */
        @Override
        public Component getTableCellRendererComponent(JTable jtable,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {

            JPropertyEditorTable table = (JPropertyEditorTable) jtable;
            Class<?> type = table.getPropertyType(row);
            if (type != null) {
                editor = editors.get(type);
                if (editor == null) {
                    editor = table.getPropertyEditor(row);

                    if (editor != null) {
                        editors.put(type, editor);
                    }
                }
            } else {
                editor = null;
            }

            if (editor != null) {
                table.initPropertyEditor(editor, row);

                editor.setValue(value);

                Component comp = editor.getCustomEditor();
                if (comp != null) {
                    comp.setEnabled(isSelected);

                    if (comp instanceof JComponent) {
                        if (isSelected)  {
                            ((JComponent) comp).setBorder(selectedBorder);
                        } else {
                            ((JComponent) comp).setBorder(emptyBorder);
                        }
                    }
                    return comp;
                }
            }
            return super.getTableCellRendererComponent(table,
                                                       value,
                                                       isSelected,
                                                       hasFocus,
                                                       row,
                                                       column);
        }

//        /**
//         * Retrieves the property editor for this value.
//         */
//          public PropertyEditor getPropertyEditor()  {
//              return editor;
//          }
    };
}
