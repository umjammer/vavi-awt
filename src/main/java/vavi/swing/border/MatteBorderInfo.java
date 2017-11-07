/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Image;
import java.beans.BeanDescriptor;

import javax.swing.border.MatteBorder;


/**
 * MatteBorderInfo.
 *
 * @beaninfo Insets "borderInsets"
 *           Integer.TYPE "top"
 *           Integer.TYPE "left"
 *           Integer.TYPE "bottom"
 *           Integer.TYPE "right"
 * @beaninfo Color "matteColor" false
 * @beaninfo Icon "tileIcon" false
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class MatteBorderInfo extends SimpleBorderInfo {

    private final Class<?> clazz = MatteBorder.class;
    private final Class<?> customizerClass = MatteBorderCustomizer.class;

    /** */
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, customizerClass);
    }

    /** */
    public Image getIcon(int iconKind) {
        return loadImage("resources/matteColorBorder.gif");
    }

    /** */
    public BorderPropertyDescriptor[] getBorderPropertyDescriptors() {
        BorderPropertyDescriptor[] bpds = new BorderPropertyDescriptor[3];
        bpds[0] = new BorderPropertyDescriptor("borderInsets", clazz);
        bpds[1] = new BorderPropertyDescriptor("matteColor", clazz);
        bpds[2] = new BorderPropertyDescriptor("tileIcon", clazz);

        return bpds;
    }
}

/* */
