/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Image;
import java.beans.BeanDescriptor;

import javax.swing.border.SoftBevelBorder;


/**
 * SoftBevelBorderInfo.
 *
 * @borderinfo Integer.TYPE "bevelType" true { LOWERED, RAISED }
 * @borderinfo Color "highlightInnerColor" false
 * @borderinfo Color "highlightOuterColor" false
 * @borderinfo Color "shadowInnerColor" false
 * @borderinfo Color "shadowOuterColor" false
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020525 nsano initial version <br>
 */
public class SoftBevelBorderInfo extends BevelBorderInfo {

    private final Class<?> clazz = SoftBevelBorder.class;
    private final Class<?> customizerClass = SoftBevelBorderCustomizer.class;

    /** */
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, customizerClass);
    }

    /** */
    public Image getIcon(int iconKind) {
        return loadImage("resources/softBevelBorder.gif");
    }
}

/* */
