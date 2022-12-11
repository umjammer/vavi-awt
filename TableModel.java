/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding.table;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.table.AbstractTableModel;

import vavi.util.Debug;


/**
 * TableModel.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022/11/26 nsano initial version <br>
 */
public class TableModel<T> extends AbstractTableModel {

    /** model */
    private List<T> entries = new ArrayList<>();

    /** row model */
    private Map<Integer, Method> columnGetterMethods;

    /** row class */
    private Class<?> rowClass;

    /** Creates a table model */
    public TableModel(Object model) {
        Iterable<T> i = Table.Util.getIterable(model);
        for (T entry : i) {
            entries.add(entry);
        }
Debug.println(Level.FINE, entries);
        rowClass = Table.Util.getRowClass(model);
Debug.println(Level.FINE, rowClass);
        columnGetterMethods = Row.Util.getGetterMethods(rowClass);
Debug.println(Level.FINE, columnGetterMethods);
    }

    @Override
    public int getColumnCount() {
        return columnGetterMethods.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        Method method = columnGetterMethods.get(columnIndex);
        String name = method.getAnnotation(Column.class).name();
        return !name.isEmpty() ? name : normalizeName(method.getName());
    }

    /** getter -> field */
    private static String normalizeName(String string) {
        return uncapitalizeFirstChar(string.replaceFirst("^(get|is)", ""));
    }

    /** */
    private static String uncapitalizeFirstChar(String string) {
        return Character.toLowerCase(string.charAt(0)) + string.substring(1);
    }

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            T row = entries.get(rowIndex);
            Object object = rowClass.getDeclaredConstructor().newInstance();
            Row.Util.setModel(object, row);
            return columnGetterMethods.get(columnIndex).invoke(object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }
}

/* */
