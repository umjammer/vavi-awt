/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.BorderLayout;
import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import static java.lang.System.getLogger;


/**
 * BorderLayoutInfo.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020527 nsano initial version <br>
 */
public class BorderLayoutInfo extends SimpleBeanInfo {

    private static final Logger logger = getLogger(BorderLayoutInfo.class.getName());

    private final Class<?> clazz = BorderLayout.class;
    private final Class<?> customizerClass = BorderLayoutCustomizer.class;

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, customizerClass);
    }

    @Override
    public Image getIcon(int iconKind) {
        return loadImage("resources/borderLayout.gif");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor[] pds = new PropertyDescriptor[2];
            pds[0] = new PropertyDescriptor("hgap", clazz);
            pds[1] = new PropertyDescriptor("vgap", clazz);

            return pds;
        } catch (IntrospectionException e) {
logger.log(Level.ERROR, e.getMessage(), e);
            return null;
        }
    }
}
