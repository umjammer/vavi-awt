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
import javax.swing.SwingConstants;


/**
 * Set {@link javax.swing.JTable} column information.
 * <ul>
 *  <li>getter must starts with <code>get</code> or <code>is</code></li>
 *  <li>setter must starts with <code>set</code></li>
 * </ul>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022/11/26 nsano initial version <br>
 */
@Target({ /* ElementType.FIELD, */ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    enum Align {
        left(SwingConstants.LEFT),
        center(SwingConstants.CENTER),
        right(SwingConstants.RIGHT);
        final int value;
        Align(int value) {
            this.value = value;
        }
    }

    /** column number 0 origin */
    int sequence();

    /** table column header name */
    String name() default "";

    /** table column width */
    int width() default 0;

    /** is the table column editable */
    boolean editable() default false;

    /** table column align */
    Align align() default Align.center;

    /**
     * TODO when annotated to method
     */
    class Util {

        private Util() {
        }

    }
}

/* */
