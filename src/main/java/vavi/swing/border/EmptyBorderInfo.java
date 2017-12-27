/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Image;
import java.beans.BeanDescriptor;

import javax.swing.border.EmptyBorder;


/**
 * EmptyBorderInfo.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class EmptyBorderInfo extends SimpleBorderInfo {

    private final Class<?> clazz = EmptyBorder.class;
    private final Class<?> customizerClass = EmptyBorderCustomizer.class;

    /** */
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, customizerClass);
    }

    public Image getIcon(int iconKind) {
        return loadImage("resources/border.gif");
    }

    /** */
    public BorderPropertyDescriptor[] getBorderPropertyDescriptors() {
        BorderPropertyDescriptor[] bpds = new BorderPropertyDescriptor[1];
        bpds[0] = new BorderPropertyDescriptor("borderInsets", clazz);
        return bpds;
    }
}

/* */
