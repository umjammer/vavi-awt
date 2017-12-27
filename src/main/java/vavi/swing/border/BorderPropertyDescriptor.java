/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Method;
import java.util.logging.Level;

import vavi.util.Debug;

/**
 * BorderPropertyDescriptor.
 * <p>
 * Border は getter メソッドが無いクラスがほとんどなので，
 * getter メソッド のみを扱う PropertyDescriptor としてこのクラスがあります．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020525 nsano initial version <br>
 */
public class BorderPropertyDescriptor extends FeatureDescriptor {

    private String propertyName;

    private Class<?> borderClass;

    /** */
    public BorderPropertyDescriptor(String propertyName, Class<?> borderClass) {
        this.propertyName = propertyName;
        this.borderClass = borderClass;

        this.setDisplayName(propertyName);
        this.setShortDescription(propertyName);
    }

    /** */
    public Method getReadMethod() {
        try {
            char c = Character.toUpperCase(propertyName.charAt(0));
            String name = "get" + c + propertyName.substring(1);
//Debug.println(name);
            Class<?>[] paramTypes = new Class[0];
            Method method = borderClass.getDeclaredMethod(name, paramTypes);
            return method;
        } catch (Exception e) {
Debug.println(Level.SEVERE, e);
            return null;
        }
    }

    /** */
    public Class<?> getPropertyType() {
//Debug.println(getReadMethod().getReturnType());
        return getReadMethod().getReturnType();
    }

    /** */
    public Class<?> getPropertyEditorClass() {
        return null;
    }
}

/* */
