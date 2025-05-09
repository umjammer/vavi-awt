/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.propertyeditor;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import vavi.swing.beaninfo.SwingEditorSupport;
import vavi.swing.beaninfo.SwingLayoutManagerEditor;

import static java.lang.System.getLogger;


/**
 * Table model used to obtain property names and values. This model encapsulates
 * an array of PropertyDescriptors.
 *
 * @author Mark Davidson
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 1.11 000105 original version <br>
 *          1.12 020525 nsano refine <br>
 */
public class PropertyDescriptorTableModel
    extends AbstractDescriptorTableModel {

    private static final Logger logger = getLogger(PropertyDescriptorTableModel.class.getName());

    private BeanInfo info;

    // Shared instance of a comparator
    private static final DescriptorComparator<FeatureDescriptor> comparator = new DescriptorComparator<>();

    // View options
    public static final int VIEW_ALL         = 0;
    public static final int VIEW_STANDARD    = 1;
    public static final int VIEW_EXPERT      = 2;
    public static final int VIEW_READ_ONLY   = 3;
    public static final int VIEW_BOUND       = 4;
    public static final int VIEW_CONSTRAINED = 5;
    public static final int VIEW_HIDDEN      = 6;
    public static final int VIEW_PREFERRED   = 7;

    // Sort options
    public static final int SORT_DEF  = 0;
    public static final int SORT_NAME = 1;
    public static final int SORT_TYPE = 2;

    public PropertyDescriptorTableModel() {
        super();
    }

    public PropertyDescriptorTableModel(Object bean) {
        super();
        setObject(bean);
    }

    /**
     * Set the table model to represents the properties of the object.
     */
    @Override
    public void setObject(Object bean) {
        super.setObject(bean);

        try {
            info = Introspector.getBeanInfo(bean.getClass());
            descriptors = info.getPropertyDescriptors();
            sortTable(SORT_NAME);
        } catch (IntrospectionException e) {
logger.log(Level.ERROR, e.getMessage(), e);
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
        if (col == JPropertyEditorTable.COL_VALUE && descriptors != null) {
            if (getPropertyDescriptor(row).getWriteMethod() == null) {
                return false;
            } else {
                return true;
            }
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
logger.log(Level.INFO, "Bean: " + bean.toString());
logger.log(Level.INFO, "Getter: " + getter.getName());
logger.log(Level.INFO, "Getter args: ");
for (int i = 0; i < args.length; i++) {
  logger.log(Level.INFO, "\t" + "type: " + paramTypes[i] + " value: " + args[i]);
}
                } catch (Exception e) {
logger.log(Level.INFO, e);
logger.log(Level.INFO, descriptors[row].getShortDescription());
logger.log(Level.INFO, "Bean: " + bean.toString());
                }
            }
        }
        return value;
    }

    /**
     * Set the value of the Values column.
     */
    @Override
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
logger.log(Level.INFO, e);
logger.log(Level.INFO, descriptors[row].getShortDescription());
logger.log(Level.INFO, "Setter: " + setter);
logger.log(Level.INFO, "Argument: " + value.getClass());
logger.log(Level.INFO, "Row: " + row + " Column: " + column);
            }
        }
    }

    // ----

    /** */
    private PropertyDescriptor getPropertyDescriptor(int row) {
        return (PropertyDescriptor) descriptors[row];
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
    @SuppressWarnings("unchecked")
    protected Class<PropertyEditor> getPropertyEditorClass(int row) {
        return (Class<PropertyEditor>) getPropertyDescriptor(row).getPropertyEditorClass();
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
        if (editor instanceof SwingLayoutManagerEditor)  {
            ((SwingLayoutManagerEditor) editor).setBean(bean);
//      } else if (editor instanceof SwingBorderEditor)  {
//          ((SwingBorderEditor) editor).setBean(bean);
        }
    }

    // ----

    /**
     * Sorts the table according to the sort type.
     */
    public void sortTable(int sort) {
        if (sort == SORT_DEF || descriptors == null)
            return;

        if (sort == SORT_NAME) {
            Arrays.sort(descriptors, comparator);
        } else {
            Arrays.sort(descriptors, comparator);
        }
        fireTableDataChanged();
    }

    /**
     * Filters the table to display only properties with specific attributes.
     * @param view The properties to display.
     */
    public void filterTable(int view) {
        if (info == null) {
            return;
        }

        descriptors = info.getPropertyDescriptors();

        // Use collections to filter out unwanted properties

        List<FeatureDescriptor> list = new ArrayList<>();

        for (FeatureDescriptor descriptor : descriptors) {
            PropertyDescriptor desc = (PropertyDescriptor) descriptor;

            switch (view) {
            case VIEW_ALL:
                if (!desc.isHidden()) {
                    list.add(desc);
                }
                break;
            case VIEW_STANDARD:
                if (desc.getWriteMethod() != null &&
                        !desc.isExpert() &&
                        !desc.isHidden()) {
                    list.add(desc);
                }
                break;
            case VIEW_EXPERT:
                if (desc.getWriteMethod() != null &&
                        desc.isExpert() &&
                        !desc.isHidden()) {
                    list.add(desc);
                }
                break;
            case VIEW_READ_ONLY:
                if (desc.getWriteMethod() == null &&
                        !desc.isHidden()) {
                    list.add(desc);
                }
                break;
            case VIEW_HIDDEN:
                if (desc.isHidden()) {
                    list.add(desc);
                }
                break;
            case VIEW_BOUND:
                if (desc.isBound() && !desc.isHidden()) {
                    list.add(desc);
                }
                break;
            case VIEW_CONSTRAINED:
                if (desc.isConstrained() && !desc.isHidden()) {
                    list.add(desc);
                }
                break;
            case VIEW_PREFERRED:
                if (desc.isPreferred() && !desc.isHidden()) {
                    list.add(desc);
                }
                break;
            }
        }

        descriptors = list.toArray(new FeatureDescriptor[0]);
        sortTable(SORT_NAME);
    }
}
