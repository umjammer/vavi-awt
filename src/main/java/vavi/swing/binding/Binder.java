/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding;

import java.lang.reflect.Field;


/**
 * Binder.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-14 nsano initial version <br>
 */
public interface Binder<T> {

    /** enable/disable a "swing" -> "bean.field" swing listener */
    void setEnabled(boolean isEnabled);

    /** set an updater which a swing listener tells to */
    void setUpdater(Updater<T> updater);

    /** Returns a class that this binder is acceptable. */
    Class<?> acceptable();

    /**
     * Binds "bean.field" and "swing" mutually.
     * "swing" -> "bean.field" is proceeded by a swing listener.
     */
    void bind(T bean, Field field, Object swing);
}
