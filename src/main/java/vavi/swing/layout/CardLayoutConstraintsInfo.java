/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * CardLayoutConstraintsInfo.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020614 nsano initial version <br>
 */
public class CardLayoutConstraintsInfo extends SimpleBeanInfo {

    private final Class<?> clazz = CardLayoutConstraints.class;

    /** */
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, null);
    }

    /** */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor[] pds = new PropertyDescriptor[1];
            pds[0] = new PropertyDescriptor("title", clazz);
            return pds;
        } catch (IntrospectionException e) {
Debug.println(Level.SEVERE, e);
            throw new IllegalStateException(e);
        }
    }
}

/* */
