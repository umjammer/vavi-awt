/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.propertyeditor;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellEditor;

import static java.lang.System.getLogger;


/**
 * An editor for types which have a property editor.
 * Note: Should consolidate with the PropertyValueRenderer
 */
public class PropertyValueEditor extends AbstractCellEditor
    implements TableCellEditor {

    private static final Logger logger = getLogger(PropertyValueEditor.class.getName());

    private PropertyEditor editor;
    private final DefaultCellEditor cellEditor;
    private Class<?> type;

    private final Border selectedBorder = BorderFactory.createLineBorder(UIManager.getColor("Table.selectionBackground"), 1);

    private final Border emptyBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);

    private final Map<Class<?>, PropertyEditor> editors;

    public PropertyValueEditor() {
        editors = new HashMap<>();
        cellEditor = new DefaultCellEditor(new JTextField());
    }

    /**
     * Get UI for current editor, including custom editor button
     * if applicable.
     */
    @Override
    public Component getTableCellEditorComponent(JTable jtable,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column) {

        JPropertyEditorTable table = (JPropertyEditorTable) jtable;
        type = table.getPropertyType(row);

        if (type != null) {
            editor = editors.get(type);
            if (editor == null) {
                PropertyEditor ed = table.getPropertyEditor(row);

                // Make a copy of this prop editor and register this as a
                // prop change listener.
                // We have to do this since we want a unique PropertyEditor
                // instance to be used for an editor vs. a renderer.
                if (ed != null) {
                    @SuppressWarnings("unchecked")
                    Class<PropertyEditor> editorClass = (Class<PropertyEditor>) ed.getClass();
                    try {
                        editor = editorClass.getDeclaredConstructor().newInstance();
                        editor.addPropertyChangeListener(pcl);
                        editors.put(type, editor);
                    } catch (Exception e) {
logger.log(Level.DEBUG, "Couldn't instantiate type editor: " + editorClass.getName());
                    }
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
                    if (isSelected) {
                        ((JComponent) comp).setBorder(selectedBorder);
                    } else {
                        ((JComponent) comp).setBorder(emptyBorder);
                    }
                }
                return comp;
            }
        }
        return cellEditor.getTableCellEditorComponent(table,
                                                      value,
                                                      isSelected,
                                                      row,
                                                      column);
    }

    /**
     * Get cellEditorValue for current editor
     * @see javax.swing.CellEditor
     */
    @Override
    public Object getCellEditorValue() {
        Object obj = null;

        if (editor != null) {
            obj = editor.getValue();
        } else {
            obj = cellEditor.getCellEditorValue();
        }

        if (type != null &&
            obj != null &&
            !type.isPrimitive() &&
            !type.isAssignableFrom(obj.getClass())) {

logger.log(Level.DEBUG, "Type mismatch: " + obj.getClass() + " type = " + type);

            try {
                obj = type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
logger.log(Level.ERROR, e.getMessage(), e);
//e.printStackTrace();
            }
        }
        return obj;
    }

    //
    // Property Change handler.
    //
    private final PropertyChangeListener pcl = ev -> stopCellEditing();
}
