/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding.table;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
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

    /** real row values {@link T} from {@link Table} annotated model */
    private final List<T> rowModels = new ArrayList<>();

    /** {@link Row} annotated row value object getter methods */
    private final Map<Integer, Method> columnGetters;

    /** {@link Row} annotated row value object setter methods */
    private final Map<Integer, Method> columnSetters = new HashMap<>();

    /** {@link Row} annotated row class */
    private final Class<?> rowClass;

    /** {@link Table} annotated bean */
    private final Object tableModelBean;

    /** {@link Row} annotated row value objects */
    private final Map<Integer, Object> rowObjects = new HashMap<>();

    /**
     * Creates a table model.
     *
     * @param model {@link Table} annotated
     */
    public TableModel(Object model) {
        this.tableModelBean = model;
        update();
        rowClass = Table.Util.getRowClass(model);
Debug.println(Level.FINE, rowClass);
        columnGetters = Row.Util.getGetterMethods(rowClass);
Debug.println(Level.FINE, columnGetters);
    }

    /** mhh... */
    public void bind(JTable table) {
        table.setModel(this);
        for (int i = 0; i < this.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            if (Row.Util.getEditable(this.rowClass, i)) {
                Method setter = Row.Util.getColumnSetterMethod(this.rowClass, i);
Debug.println(Level.FINE, "setter: " + i + ", " + setter);
                columnSetters.put(1, setter);
            }
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

        // JTable default renderer normally deals images for only IconImage.
        table.setDefaultRenderer(BufferedImage.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = table.getDefaultRenderer(ImageIcon.class).getTableCellRendererComponent(
                        table, new ImageIcon((BufferedImage) value), isSelected, hasFocus, row, column);
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    ImageIO.write((BufferedImage) value, "png", baos);
                    // see vavi.net.www.DataURLStreamHandler for url data:...
                    ((JComponent) c).setToolTipText(String.format(
                            "<html><img src='data:image/png;base64,%s'></html>",
                            Base64.getEncoder().encodeToString(baos.toByteArray())));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
                return c;
            }
        });

        ToolTipManager.sharedInstance().setDismissDelay(10 * 60 * 1000);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return columnSetters.containsKey(col);
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        SwingUtilities.invokeLater(() -> {
            Method setter = columnSetters.get(col);
            if (row < rowModels.size()) {
                T o = rowModels.get(row);
                if (o != null) {
                    try {
Debug.println("setter: " + setter.getName() + ", col: " + col + ", row: " + row + ", value: " + value);
                        Object vo = getRowObject(row);
                        Row.Util.setModel(vo, o);
                        setter.invoke(vo, value);
                        fireTableCellUpdated(row, col);
                        return;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        Debug.printStackTrace(e);
                    }
                }
            }
Debug.println(Level.WARNING, "no setter for column: " + col + ", row: " + row);
        });
    }

    /** fill real row values {@link T} from {@link Table} annotated model */
    void fill() {
        synchronized (rowModels) {
            rowModels.clear();
            Iterable<T> i = Table.Util.getIterable(tableModelBean);
            for (T entry : i) {
                rowModels.add(entry);
            }
Debug.println(Level.FINER, rowModels);
        }
    }

    /** fill real row values {@link T} from {@link Table} annotated model and tell table */
    public final void update() {
        SwingUtilities.invokeLater(() -> {
            fill();
            fireTableDataChanged();
        });
    }

    @Override
    public int getColumnCount() {
        return columnGetters.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        Method method = columnGetters.get(columnIndex);
        String name = method.getAnnotation(Column.class).name();
        return !name.isEmpty() ? name : normalizeName(method.getName());
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Method method = columnGetters.get(columnIndex);
Debug.println(Level.FINER, columnIndex + ": " + method.getReturnType().getName());
        return method.getReturnType();
    }

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
        return rowModels.size();
    }

    /** get row value object at rowIndex */
    private Object getRowObject(int rowIndex) {
        try {
            Object vo = rowObjects.get(rowIndex);
            if (vo == null) {
                vo = rowClass.getDeclaredConstructor().newInstance();
                rowObjects.put(rowIndex, vo);
            }
            return vo;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            synchronized (rowModels) {
                if (rowIndex > rowModels.size()) return null;
                T row = rowModels.get(rowIndex);
                Object rowObject = getRowObject(rowIndex);
                Row.Util.setModel(rowObject, row);
                return columnGetters.get(columnIndex).invoke(rowObject);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}

/* */
