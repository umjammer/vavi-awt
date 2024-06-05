/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Image;
import java.beans.BeanDescriptor;

import javax.swing.border.BevelBorder;


/**
 * BevelBorderInfo.
 *
 * @beaninfo Integer.TYPE "bevelType" true { LOWERED, RAISED }
 * @beaninfo Color "highlightInnerColor" false
 * @beaninfo Color "highlightOuterColor" false
 * @beaninfo Color "shadowInnerColor"    false
 * @beaninfo Color "shadowOuterColor"    false
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class BevelBorderInfo extends SimpleBorderInfo {

    private final Class<?> clazz = BevelBorder.class;
    private final Class<?> customizerClass = BevelBorderCustomizer.class;

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, customizerClass);
    }

    @Override
    public Image getIcon(int iconKind) {
        return loadImage("resources/bevelBorder.gif");
    }

    @Override
    public BorderPropertyDescriptor[] getBorderPropertyDescriptors() {
        BorderPropertyDescriptor[] bpds = new BorderPropertyDescriptor[5];
        bpds[0] = new BorderPropertyDescriptor("bevelType", clazz);
        Object[] value = new Object[] {
            "RAISED", BevelBorder.RAISED, null,
            "LOWERED", BevelBorder.LOWERED, null,
        };
        bpds[0].setValue("enumerationValues", value);
        bpds[1] = new BorderPropertyDescriptor("highlightInnerColor", clazz);
        bpds[2] = new BorderPropertyDescriptor("highlightOuterColor", clazz);
        bpds[3] = new BorderPropertyDescriptor("shadowInnerColor", clazz);
        bpds[4] = new BorderPropertyDescriptor("shadowOuterColor", clazz);

        return bpds;
    }
}
