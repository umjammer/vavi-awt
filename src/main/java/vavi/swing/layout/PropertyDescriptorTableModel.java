/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import javax.swing.event.TableModelEvent;

import vavi.swing.beaninfo.SwingEditorSupport;
import vavi.swing.propertyeditor.AbstractDescriptorTableModel;
import vavi.swing.propertyeditor.JPropertyEditorTable;
import vavi.util.Debug;


/**
 * Table model used to obtain property names and values.
 * This model encapsulates an array of PropertyDescriptors.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 */
public class PropertyDescriptorTableModel
    extends AbstractDescriptorTableModel {

    /**
     * Set the table model to represents the properties of the object.
     */
    public void setObject(Object layout) {
        super.setObject(layout);

        BeanInfo info =
            LayoutManagerInfoFactory.getBeanInfo(layout.getClass());

        if (info != null) {
            descriptors = info.getPropertyDescriptors();
            fireTableDataChanged();
        }
    }

    //-------------------------------------------------------------------------

    /**
     * Check if given cell is editable
     * @param row table row
     * @param col table column
     */
    public boolean isCellEditable(int row, int col) {
        if (col == JPropertyEditorTable.COL_VALUE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get text value for cell of table
     * @param row table row
     * @param col table column
     */
    public Object getValueAt(int row, int col) {

        Object value = null;

        if (col == JPropertyEditorTable.COL_NAME) {
            value = descriptors[row].getDisplayName();
        }
        else {
            // COL_VALUE is handled
            Method getter = getPropertyDescriptor(row).getReadMethod();

            if (getter != null) {
                Class<?>[] paramTypes = getter.getParameterTypes();
                Object[] args = new Object[paramTypes.length];

                try {
                    for (int i = 0; i < paramTypes.length; i++) {
                        // XXX - debug
                        args[i] = paramTypes[i].newInstance();
                        throw new IllegalStateException(
                            "getValueAt getter = " + getter +
                            " parameter = " + paramTypes[i]);
                    }
                } catch (Exception e) {
                    throw new NoSuchElementException(
                        col + ", " + row + ": " + e);
                }

                try {
                    value = getter.invoke(bean, args);
                } catch (NoSuchMethodError e) {
                    // XXX - handle better
Debug.println(Level.INFO, e);
Debug.println(Level.INFO, descriptors[row].getShortDescription());
Debug.println(Level.INFO, "LayoutManager: " + bean.toString());
Debug.println(Level.INFO, "Getter: " + getter.getName());
Debug.println(Level.INFO, "Getter args: ");
for (int i = 0; i < args.length; i++) {
  Debug.println(Level.INFO, "\t" + "type: " + paramTypes[i] + " value: " + args[i]);
}
                } catch (Exception e) {
Debug.println(Level.INFO, e);
Debug.println(Level.INFO, descriptors[row].getShortDescription());
Debug.println(Level.INFO, "LayoutManager: " + bean.toString());
                }
            }
        }
        return value;
    }

    /**
     * Set the value of the Values column.
     */
    public void setValueAt(Object value, int row, int column) {

        if (column != JPropertyEditorTable.COL_VALUE ||
            descriptors == null ||
            row > descriptors.length) {
            return;
        }

        Method setter = getPropertyDescriptor(row).getWriteMethod();
        if (setter != null) {
            try {
                setter.invoke(bean, value);
            } catch (Exception e) {
Debug.println(Level.INFO, e);
Debug.println(Level.INFO, descriptors[row].getShortDescription());
Debug.println(Level.INFO, "Setter: " + setter);
Debug.println(Level.INFO, "Argument: " + value.getClass().toString());
Debug.println(Level.INFO, "Row: " + row + " Column: " + column);
            }
        }

        fireTableChanged(new TableModelEvent(this, row));
    }

    //-------------------------------------------------------------------------

    /** */
    private PropertyDescriptor getPropertyDescriptor(int row) {
        return (PropertyDescriptor) descriptors[row];
    }

    /**
     * Returns the Java type info for the property at the given row.
     */
    public Class<?> getPropertyType(int row) {
        return getPropertyDescriptor(row).getPropertyType();
    }

    /**
     * Returns a new instance of the property editor for a given class. If an
     * editor is not specified in the property descriptor then it is looked up
     * in the PropertyEditorManager.
     */
    public Class<?> getPropertyEditorClass(int row) {
        return getPropertyDescriptor(row).getPropertyEditorClass();
    }

    /**
     * Special case for the enumerated properties.
     * Must reinitialize to reset the combo box values. TODO
     */
    public void initPropertyEditor(PropertyEditor editor, int row) {
        if (editor instanceof SwingEditorSupport)  {
            ((SwingEditorSupport) editor).init(getPropertyDescriptor(row));
        }
    }
}

/* */
