/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Image;
import java.beans.BeanDescriptor;

import javax.swing.border.LineBorder;


/**
 * LineBorderInfo.
 *
 * @beaninfo Color "color" false
 * @beaninfo Integer.TYPE "thickness"
 * @beaninfo Boolean.TYPE "roundedCorners"
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class LineBorderInfo extends SimpleBorderInfo {

    private final Class<?> clazz = LineBorder.class;
    private final Class<?> customizerClass = LineBorderCustomizer.class;

    /** */
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, customizerClass);
    }

    /** */
    public Image getIcon(int iconKind) {
        return loadImage("resources/lineBorder.gif");
    }

    /** */
    public BorderPropertyDescriptor[] getBorderPropertyDescriptors() {
        BorderPropertyDescriptor[] bpds = new BorderPropertyDescriptor[3];
        bpds[0] = new BorderPropertyDescriptor("lineColor", clazz);
        bpds[1] = new BorderPropertyDescriptor("thickness", clazz);
        bpds[2] = new BorderPropertyDescriptor("roundedCorners", clazz);

        return bpds;
    }
}
