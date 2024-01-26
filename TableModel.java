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
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import vavi.util.Debug;


/**
 * TableModel.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022/11/26 nsano initial version <br>
 */
public class TableModel<T> extends AbstractTableModel {

    /** model */
    private final List<T> entries = new ArrayList<>();

    /** row model */
    private final Map<Integer, Method> columnGetterMethods;

    /** row class */
    private final Class<?> rowClass;

    /** */
    private Object model;

    /** Creates a table model */
    public TableModel(Object model) {
        this.model = model;
        update();
        rowClass = Table.Util.getRowClass(model);
Debug.println(Level.FINE, rowClass);
        columnGetterMethods = Row.Util.getGetterMethods(rowClass);
Debug.println(Level.FINE, columnGetterMethods);
    }

    /** mhh... */
    public void bind(JTable table) {
        table.setModel(this);
        for (int i = 0; i < this.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            int width = Row.Util.getWidth(this.rowClass, i);
            Column.Align align = Row.Util.getAlign(this.rowClass, i);
Debug.println(Level.FINE, i + ": " + width + ", " + align);
            column.setPreferredWidth(width);
            if (align != Column.Align.center) {
                DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
                renderer.setHorizontalAlignment(align.value);
                column.setCellRenderer(renderer);
            }
        }
    }

    /** */
    public final void update() {
        synchronized (entries) {
            entries.clear();
            Iterable<T> i = Table.Util.getIterable(model);
            for (T entry : i) {
                entries.add(entry);
            }
Debug.println(Level.FINE, entries);
        }
        fireTableDataChanged();
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

//    @Override
//    public Class<?> getColumnClass(int columnIndex) {
//        Method method = columnGetterMethods.get(columnIndex);
//Debug.println(Level.FINER, columnIndex + ": " + method.getReturnType().getName());
//        return method.getReturnType();
//    }

    /** getter -> field */
    private static String normalizeName(String string) {
        return uncapitalizeFirstChar(string.replaceFirst("^(get|is)", ""));
    }

    /** capitalize a first letter */
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
            synchronized (entries) {
                if (rowIndex > entries.size()) return null;
                T row = entries.get(rowIndex);
                Object object = rowClass.getDeclaredConstructor().newInstance();
                Row.Util.setModel(object, row);
                return columnGetterMethods.get(columnIndex).invoke(object);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }
}

/* */
