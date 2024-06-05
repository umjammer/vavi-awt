/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.BorderLayout;
import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * BorderLayoutConstraintsInfo.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 */
public class BorderLayoutConstraintsInfo extends SimpleBeanInfo {

    private final Class<?> clazz = BorderLayoutConstraints.class;

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, null);
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor[] pds = new PropertyDescriptor[1];
            pds[0] = new PropertyDescriptor("direction", clazz);
            Object[] value = new Object[] {
                "AFTER_LAST_LINE", BorderLayout.AFTER_LAST_LINE, null,
                "AFTER_LINE_ENDS", BorderLayout.AFTER_LINE_ENDS, null,
                "BEFORE_FIRST_LINE", BorderLayout.BEFORE_FIRST_LINE, null,
                "BEFORE_LINE_BEGINS", BorderLayout.BEFORE_LINE_BEGINS, null,
                "CENTER", BorderLayout.CENTER, null,
                "EAST", BorderLayout.EAST, null,
                "NORTH", BorderLayout.NORTH, null,
                "SOUTH", BorderLayout.SOUTH, null,
                "WEST", BorderLayout.WEST, null,
            };
            pds[0].setValue("enumerationValues", value);
            return pds;
        } catch (IntrospectionException e) {
Debug.println(Level.SEVERE, e);
            throw new IllegalStateException(e);
        }
    }
}
