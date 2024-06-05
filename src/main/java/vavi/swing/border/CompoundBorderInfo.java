/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Image;
import java.beans.BeanDescriptor;

import javax.swing.border.CompoundBorder;


/**
 * CompoundBorderInfo.
 *
 * @beaninfo Border "insideBorder"
 * @beaninfo Border "outsideBorder"
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class CompoundBorderInfo extends SimpleBorderInfo {

    private final Class<?> clazz = CompoundBorder.class;
    private final Class<?> customizerClass = CompoundBorderCustomizer.class;

    /** */
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, customizerClass);
    }

    /** */
    public Image getIcon(int iconKind) {
        return loadImage("resources/compoundBorder.gif");
    }

    /** */
    public BorderPropertyDescriptor[] getBorderPropertyDescriptors() {
        BorderPropertyDescriptor[] bpds = new BorderPropertyDescriptor[2];
        bpds[0] = new BorderPropertyDescriptor("insideBorder", clazz);
        bpds[1] = new BorderPropertyDescriptor("outsideBorder", clazz);

        return bpds;
    }
}
