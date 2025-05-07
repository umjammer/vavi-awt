/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.CardLayout;
import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import static java.lang.System.getLogger;


/**
 * CardLayoutInfo.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020527 nsano initial version <br>
 */
public class CardLayoutInfo extends SimpleBeanInfo {

    private static final Logger logger = getLogger(CardLayoutInfo.class.getName());

    private final Class<?> clazz = CardLayout.class;
    private final Class<?> customizerClass = CardLayoutCustomizer.class;

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, customizerClass);
    }

    @Override
    public Image getIcon(int iconKind) {
        return loadImage("resources/cardLayout.gif");
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
