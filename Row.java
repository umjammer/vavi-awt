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
import java.util.function.Function;


/**
 * Row.
 *
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
            return getColumnMethod(row, columnIndex, Column::width);
        }

        /**
         * @see Column#align()
         */
        public static Column.Align getAlign(Class<?> row, int columnIndex) {
            return getColumnMethod(row, columnIndex, Column::align);
        }

        /**
         * @see Column
         */
        public static <T> T getColumnMethod(Class<?> row, int columnIndex, Function<Column, T> supplier) {
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
                        if (column.sequence() == columnIndex &&
                                (method.getName().startsWith("get") || method.getName().startsWith("is"))) {
                            return supplier.apply(column);
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
