/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.beans.FeatureDescriptor;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Method;

import static java.lang.System.getLogger;


/**
 * BorderPropertyDescriptor.
 * <p>
 * Since most Border classes do not have getter methods,
 * this class exists as a PropertyDescriptor that only handles the getter method.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020525 nsano initial version <br>
 */
public class BorderPropertyDescriptor extends FeatureDescriptor {

    private static final Logger logger = getLogger(BorderPropertyDescriptor.class.getName());

    private final String propertyName;

    private final Class<?> borderClass;

    /** */
    public BorderPropertyDescriptor(String propertyName, Class<?> borderClass) {
        this.propertyName = propertyName;
        this.borderClass = borderClass;

        this.setDisplayName(propertyName);
        this.setShortDescription(propertyName);
    }

    /** @return null when error */
    public Method getReadMethod() {
        try {
            char c = Character.toUpperCase(propertyName.charAt(0));
            String name = "get" + c + propertyName.substring(1);
logger.log(Level.TRACE, name);
            Class<?>[] paramTypes = new Class[0];
            Method method = borderClass.getDeclaredMethod(name, paramTypes);
            return method;
        } catch (Exception e) {
logger.log(Level.ERROR, e.getMessage(), e);
            return null;
        }
    }

    /** */
    public Class<?> getPropertyType() {
logger.log(Level.TRACE, getReadMethod().getReturnType());
        return getReadMethod().getReturnType();
    }

    /** */
    public Class<?> getPropertyEditorClass() {
        return null;
    }
}
