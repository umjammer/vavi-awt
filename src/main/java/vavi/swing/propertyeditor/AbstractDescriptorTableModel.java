/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.propertyeditor;

import java.beans.FeatureDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.table.AbstractTableModel;

import vavi.util.Debug;


/**
 * Table model used to obtain property names and values. This model encapsulates
 * an array of PropertyDescriptors.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020525 nsano initial version <br>
 */
public abstract class AbstractDescriptorTableModel extends AbstractTableModel {

    protected FeatureDescriptor[] descriptors;

    protected Object bean;

    /** Cached property editors. */
    private final Map<Class<?>, PropertyEditor> propEditors;

    private static final int NUM_COLUMNS = 2;

    public AbstractDescriptorTableModel() {
        propEditors = new HashMap<>();
    }

    /**
     * Set the table model to represents the properties of the object.
     */
    public void setObject(Object bean) {
        this.bean = bean;
    }

    /**
     * Return the current object that is represented by this model.
     */
    public Object getObject() {
        return bean;
    }

    //-------------------------------------------------------------------------

    /**
     * Get row count (total number of properties shown)
     */
    @Override
    public int getRowCount() {
        if (descriptors == null) {
            return 0;
        }

        return descriptors.length;
    }

    /**
     * Get column count (2: name, value)
     */
    @Override
    public int getColumnCount() {
        return NUM_COLUMNS;
    }

    //-------------------------------------------------------------------------

    /**
     * Returns the PropertyDescriptor for the row.
     */
    public String getShortDescription(int row) {
        return descriptors[row].getShortDescription();
    }

    /**
     * Returns the Java type info for the property at the given row.
     */
    public abstract Class<?> getPropertyType(int row);

    /**
     */
    protected abstract Class<?> getPropertyEditorClass(int row);

    /**
     * Returns a new instance of the property editor for a given class. If an
     * editor is not specified in the property descriptor then it is looked up
     * in the PropertyEditorManager.
     */
    public PropertyEditor getPropertyEditor(int row) {
        Class<?> clazz = getPropertyEditorClass(row);

        PropertyEditor editor = null;

        if (clazz != null) {
            try {
                editor = (PropertyEditor) clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
Debug.println(Level.SEVERE, "creating PropertyEditor: " + e);
            }
        } else {
            // Look for a registered editor for this type.
            Class<?> type = getPropertyType(row);
            if (type != null) {
                editor = propEditors.get(type);

                if (editor == null) {
                    // Load a shared instance of the property editor.
                    editor = PropertyEditorManager.findEditor(type);
                    if (editor != null) {
                        propEditors.put(type, editor);
                    }
                }

                if (editor == null) {
                    // Use the editor for Object.class
                    editor = propEditors.get(Object.class);
                    if (editor == null) {
                        editor = PropertyEditorManager.findEditor(Object.class);
                        if (editor != null) {
                            propEditors.put(Object.class, editor);
                        }
                    }

                }
            }
        }

        return editor;
    }

    /**
     * Special case for the enumerated properties.
     * Must reinitialize to reset the combo box values. TODO
     */
    public abstract void initPropertyEditor(PropertyEditor editor, int row);
}
