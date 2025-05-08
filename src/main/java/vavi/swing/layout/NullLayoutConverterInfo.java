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
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import static java.lang.System.getLogger;


/**
 * NullLayoutConverterInfo.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020617 nsano initial version <br>
 */
public class NullLayoutConverterInfo extends SimpleBeanInfo {

    private static final Logger logger = getLogger(NullLayoutConverterInfo.class.getName());

    private final Class<?> clazz = NullLayoutConverter.class;

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, null);
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor[] pds = new PropertyDescriptor[3];
//          pds[0] = new PropertyDescriptor("savingAsGridBagLayout", clazz);
            pds[0] = new PropertyDescriptor("overlaid", clazz);
            pds[1] = new PropertyDescriptor("justifyGrid", clazz);
            pds[2] = new PropertyDescriptor("gridSize", clazz);

            return pds;
        } catch (IntrospectionException e) {
logger.log(Level.ERROR, e.getMessage(), e);
            return null;
        }
    }
}
