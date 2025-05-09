/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

import vavi.swing.beaninfo.SwingEditorSupport;
import vavi.swing.propertyeditor.AbstractDescriptorTableModel;
import vavi.swing.propertyeditor.JPropertyEditorTable;

import static java.lang.System.getLogger;


/**
 * This is a TableModel that handles properties of the Border class.
 * The getter is obtained from BorderPropertyDescriptor.
 * Since there is no setter, the value is passed to the user via PropertyChangeEvent.
 * The user creates a new instance of Border with the arguments of the PropertyChangeEvent.
 *
 * @event PropertyChangeEvent("propertyName", newValue)
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class BorderPropertyDescriptorTableModel
    extends AbstractDescriptorTableModel {

    private static final Logger logger = getLogger(BorderPropertyDescriptorTableModel.class.getName());

    /**
     * Set the table model to represents the properties of the object.
     */
    @Override
    public void setObject(Object border) {
        super.setObject(border);

        BorderInfo info = BorderInfoFactory.getBorderInfo(border.getClass());

        if (info != null) {
            descriptors = info.getBorderPropertyDescriptors();
            fireTableDataChanged();
        }
    }

    // ----

    /**
     * Check if given cell is editable
     * @param row table row
     * @param col table column
     */
    @Override
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
    @Override
    public Object getValueAt(int row, int col) {

        Object value = null;

        if (col == JPropertyEditorTable.COL_NAME) {
            value = descriptors[row].getDisplayName();
        } else {
            // COL_VALUE is handled
            Method getter = getPropertyDescriptor(row).getReadMethod();

            if (getter != null) {
                Class<?>[] paramTypes = getter.getParameterTypes();
                Object[] args = new Object[paramTypes.length];

                try {
                    for (int i = 0; i < paramTypes.length; i++) {
                        // XXX - debug
                        args[i] = paramTypes[i].getDeclaredConstructor().newInstance();
                        throw new IllegalStateException("getValueAt getter = " + getter + " parameter = " + paramTypes[i]);
                    }
                } catch (Exception e) {
                    throw new NoSuchElementException(col + ", " + row + ": " + e);
                }

                try {
                    value = getter.invoke(bean, args);
                } catch (NoSuchMethodError e) {
                    // XXX - handle better
logger.log(Level.INFO, e);
logger.log(Level.INFO, descriptors[row].getShortDescription());
logger.log(Level.INFO, "Border: " + bean.toString());
logger.log(Level.INFO, "Getter: " + getter.getName());
logger.log(Level.INFO, "Getter args: ");
for (int i = 0; i < args.length; i++) {
  logger.log(Level.INFO, "\t" + "type: " + paramTypes[i] + " value: " + args[i]);
}
                } catch (Exception e) {
logger.log(Level.INFO, e);
logger.log(Level.INFO, descriptors[row].getShortDescription());
logger.log(Level.INFO, "Border: " + bean.toString());
                }
            }
        }
        return value;
    }

    /**
     * Set the value of the Values column.
     * TODO oldValue is null
     */
    @Override
    public void setValueAt(Object value, int row, int column) {

        if (column != JPropertyEditorTable.COL_VALUE ||
            descriptors == null ||
            row > descriptors.length) {
            return;
        }

        firePropertyChange(descriptors[row].getShortDescription(),null,value);
    }

    // ----

    /** */
    private BorderPropertyDescriptor getPropertyDescriptor(int row) {
        return (BorderPropertyDescriptor) descriptors[row];
    }

    /**
     * Returns the Java type info for the property at the given row.
     */
    @Override
    public Class<?> getPropertyType(int row) {
        return getPropertyDescriptor(row).getPropertyType();
    }

    /**
     * Returns a new instance of the property editor for a given class. If an
     * editor is not specified in the property descriptor then it is looked up
     * in the PropertyEditorManager.
     */
    @Override
    public Class<?> getPropertyEditorClass(int row) {
        return getPropertyDescriptor(row).getPropertyEditorClass();
    }

    /**
     * Special case for the enumerated properties.
     * Must reinitialize to reset the combo box values. TODO
     */
    @Override
    public void initPropertyEditor(PropertyEditor editor, int row) {
        if (editor instanceof SwingEditorSupport)  {
            ((SwingEditorSupport) editor).init(getPropertyDescriptor(row));
        }

        // TODO
//      if (editor instanceof SwingLayoutManagerEditor)  {
//          ((SwingLayoutManagerEditor) editor).setBean(bean);
//      }
    }

    // ----

    /** */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /** */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /** */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /** */
    protected void firePropertyChange(String name, Object oldValue, Object newValue) {
        pcs.firePropertyChange(name, oldValue, newValue);
    }
}
