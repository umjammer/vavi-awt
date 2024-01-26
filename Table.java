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
import java.util.NoSuchElementException;


/**
 * Table.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022/11/26 nsano initial version <br>
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /** rows method */
    String iterable();

    /** rowClass class */
    Class<?> row();

    /**
     * TODO when annotated to method
     */
    class Util {

        private Util() {
        }

        /**
         * @return a {@link Row} annotated class
         */
        public static Class<?> getRowClass(Object bean) {
            //
            Table annotation = bean.getClass().getAnnotation(Table.class);
            if (annotation == null) {
                throw new IllegalArgumentException("bean is not annotated with @Table");
            }

            //
            Class<?> clazz = bean.getClass();
            while (clazz != null) {
                for (Class<?> c : clazz.getDeclaredClasses()) {
                    Row row = c.getAnnotation(Row.class);
                    if (row != null) {
                        return c;
                    }
                }
                clazz = clazz.getSuperclass();
            }

            throw new NoSuchElementException("@Row");
        }

        /**
         * @see Table#iterable()
         */
        @SuppressWarnings("unchecked")
        public static <T> Iterable<T> getIterable(Object bean) {
            //
            Table annotation = bean.getClass().getAnnotation(Table.class);
            if (annotation == null) {
                throw new IllegalArgumentException("bean is not annotated with @Table");
            }

            String name = annotation.iterable();

            Class<?> clazz = bean.getClass();
            while (clazz != null) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getName().equals(name)) {
                        try {
                            return (Iterable<T>) method.invoke(bean);
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
