/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Image;
import java.beans.BeanDescriptor;

import javax.swing.border.EtchedBorder;


/**
 * EtchedBorderInfo.
 *
 * @beaninfo int "etchType" { LOWERED, RAISED }
 * @beaninfo Color "highlightColor"
 * @beaninfo Color "shadowColor"
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class EtchedBorderInfo extends SimpleBorderInfo {

    private final Class<?> clazz = EtchedBorder.class;
    private final Class<?> customizerClass = EtchedBorderCustomizer.class;

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, customizerClass);
    }

    @Override
    public Image getIcon(int iconKind) {
        return loadImage("resources/etchedBorder.gif");
    }

    @Override
    public BorderPropertyDescriptor[] getBorderPropertyDescriptors() {
        BorderPropertyDescriptor[] bpds = new BorderPropertyDescriptor[3];
        bpds[0] = new BorderPropertyDescriptor("etchType", clazz);
        Object[] value = new Object[] {
            "RAISED", EtchedBorder.RAISED, null,
            "LOWERED", EtchedBorder.LOWERED, null,
        };
        bpds[0].setValue("enumerationValues", value);
        bpds[1] = new BorderPropertyDescriptor("highlightColor", clazz);
        bpds[2] = new BorderPropertyDescriptor("shadowColor", clazz);

        return bpds;
    }
}
