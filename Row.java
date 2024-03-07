/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * Set {@link javax.swing.JTable} row information by {@link Column} annotation and
 * setter method for value object for a row by {@link #setter()} method.
 * <ul>
 *  <li>setter must have one parameter type is {@link Table#row()} class</li>
 *  <li>use getter returns {@link java.awt.image.BufferedImage} for Image column display</li>
 * </ul>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022/11/26 nsano initial version <br>
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Row {

    /** */
    String setter();

    /** */
    class Util {

        private Util() {
        }

        /**
         * @return {@link Column} annotated methods indexed by {@link Column#sequence()}
         */
        public static Map<Integer, Method> getGetterMethods(Class<?> row) {
            //
            Row annotation = row.getAnnotation(Row.class);
            if (annotation == null) {
                throw new IllegalArgumentException("bean is not annotated with @Row");
            }

            //
            Map<Integer, Method> getterMethods = new HashMap<>();

            Class<?> clazz = row;
            while (clazz != null) {
                for (Method method : clazz.getDeclaredMethods()) {
                    Column column = method.getAnnotation(Column.class);
                    if (column != null) {
                        if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
                            getterMethods.put(column.sequence(), method);
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }

            return getterMethods;
        }

        /**
         * @see Column#width()
         */
        public static int getWidth(Class<?> row, int columnIndex) {
            return getColumnGetterAttribute(row, columnIndex, Column::width);
        }

        /**
         * @see Column#align()
         */
        public static Column.Align getAlign(Class<?> row, int columnIndex) {
            return getColumnGetterAttribute(row, columnIndex, Column::align);
        }

        /**
         * @see Column#editable()
         */
        public static boolean getEditable(Class<?> row, int columnIndex) {
            return getColumnGetterAttribute(row, columnIndex, Column::editable);
        }

        /**
         * @see Column
         */
        public static <T> T getColumnGetterAttribute(Class<?> row, int columnIndex, Function<Column, T> attribute) {
            return getColumnAttribute(row, columnIndex, (column, m) -> attribute.apply(column),
                    method -> method.getName().startsWith("get") || method.getName().startsWith("is"));
        }

        /**
         * @see Column
         */
        public static Method getColumnSetterMethod(Class<?> row, int columnIndex) {
            return getColumnAttribute(row, columnIndex, (c, method) -> method,
                    method -> method.getName().startsWith("set"));
        }

        /** */
        public static <R> R getColumnAttribute(Class<?> row, int columnIndex, BiFunction<Column, Method, R> makeResult,
                                               Function<Method, Boolean> availableName) {
            //
            Row annotation = row.getAnnotation(Row.class);
            if (annotation == null) {
                throw new IllegalArgumentException("bean is not annotated with @Row");
            }

            //
            Class<?> clazz = row;
            while (clazz != null) {
                for (Method method : clazz.getDeclaredMethods()) {
                    Column column = method.getAnnotation(Column.class);
                    if (column != null) {
                        if (column.sequence() == columnIndex && availableName.apply(method)) {
                            return makeResult.apply(column, method);
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }

            throw new NoSuchElementException(String.valueOf(columnIndex));
        }

        /**
         * @see Row#setter()
         */
        public static <T> void setModel(Object row, T bean) {
            //
            Row annotation = row.getClass().getAnnotation(Row.class);
            if (annotation == null) {
                throw new IllegalArgumentException("bean is not annotated with @Row");
            }

            String name = annotation.setter();

            Class<?> clazz = row.getClass();
            while (clazz != null) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getName().equals(name)) {
                        try {
                            method.invoke(row, bean);
                            return;
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }

            throw new NoSuchElementException(name);
        }
    }
}

/* */
