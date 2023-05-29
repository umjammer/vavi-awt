/*
 * Copyright (c) 2017 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;


/**
 * Component.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 Oct 29, 2017 umjammer initial version <br>
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

    /** */
    String name();

    /** */
    String[] args() default "";

    /** */
    class Util {

        private Util() {
        }

        /**
         * @param field {@link Component} annotated
         * @return When {@link Component#name()} is not set, the field name will be return.
         */
        public static String getName(Field field) {
            Component target = field.getAnnotation(Component.class);
            if (target == null) {
                throw new IllegalArgumentException("bean is not annotated with @Component");
            }

            return target.name();
        }

        /**
         * @param field {@link Component} annotated
         * @return {@link Component#args()}
         */
        public static String[] getArgs(Field field) {
            Component target = field.getAnnotation(Component.class);
            if (target == null) {
                throw new IllegalArgumentException("bean is not annotated with @Component");
            }

            return target.args();
        }
    }
}

/* */
