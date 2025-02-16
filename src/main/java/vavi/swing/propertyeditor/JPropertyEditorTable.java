/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.propertyeditor;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import vavi.util.Debug;


/**
 * The UI for listing, sorting, and setting component properties.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 *          0.10 020603 nsano use properties file <br>
 */
public class JPropertyEditorTable extends JTable {

    /** */
    private final AbstractDescriptorTableModel tableModel;

    public static final int COL_NAME = 0;
    public static final int COL_VALUE = 1;

    private static final int ROW_HEIGHT = 20;

    /** */
    public JPropertyEditorTable(AbstractDescriptorTableModel tableModel) {

        this.tableModel = tableModel;
        tableModel.addTableModelListener(tml);

        PropertyColumnModel columnModel = new PropertyColumnModel();

        this.setModel(tableModel);
        this.setColumnModel(columnModel);
        this.setRowHeight(ROW_HEIGHT);
        this.setAutoResizeMode(AUTO_RESIZE_LAST_COLUMN);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Table Model Listener methods
     */
    private final TableModelListener tml = new TableModelListener() {
        @Override public void tableChanged(TableModelEvent evt)  {
            // Adjust the preferred height of the row to the same as
            // the property editor.
            setRowHeight(ROW_HEIGHT);

            PropertyEditor editor;
            Component comp;
            Dimension prefSize;

            for (int i = 0; i < getRowCount(); i++) {
                editor = tableModel.getPropertyEditor(i);
                if (editor != null)  {
                    comp = editor.getCustomEditor();
                    if (comp != null)  {
                        prefSize = comp.getPreferredSize();
                        if (prefSize.height > ROW_HEIGHT)  {
                            setRowHeight(i, prefSize.height);
                        }
                    }
                }
            }
        }
    };

    // for classes in this package ----

    /** convenience */
    public Class<?> getPropertyType(int row) {
        return tableModel.getPropertyType(row);
    }

    /** convenience */
    public String getShortDescription(int row) {
        return tableModel.getShortDescription(row);
    }

    /** convenience */
    public PropertyEditor getPropertyEditor(int row) {
        return tableModel.getPropertyEditor(row);
    }

    /** convenience */
    public void initPropertyEditor(PropertyEditor editor, int row) {
        tableModel.initPropertyEditor(editor, row);
    }

    // ----

    /*
     * Method which registers property editors for types.
     */
    static {
        Properties props = new Properties();
        final String path = "propertyEditor.properties";
        final Class<?> c = JPropertyEditorTable.class;

        try {
            props.load(c.getResourceAsStream(path));

            int i = 0;
            while (true) {
                String key = "clazz." + i;
                String value = props.getProperty(key);
                if (value == null) {
Debug.println(Level.FINE, "no property for: clazz." + i);
                    break;
                }

                Class<?> clazz = switch (value) {
                    case "boolean" -> Boolean.TYPE;
                    case "byte" -> Byte.TYPE;
                    case "char" -> Character.TYPE;
                    case "double" -> Double.TYPE;
                    case "float" -> Float.TYPE;
                    case "int" -> Integer.TYPE;
                    case "long" -> Long.TYPE;
                    case "short" -> Short.TYPE;
                    case "void" -> Void.TYPE;
                    default -> Class.forName(value);
                };

                key = "editor." + i;
                value = props.getProperty(key);

                Class<?> editorClass = Class.forName(value);

Debug.println(Level.FINER, i + ": " + clazz + ": " + editorClass);
                PropertyEditorManager.registerEditor(clazz, editorClass);

                i++;
            }
        } catch (Exception e) {
Debug.println(Level.SEVERE, e);
            throw new IllegalStateException(e);
        }
    }
}
