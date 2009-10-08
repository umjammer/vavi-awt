/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.SimpleBeanInfo;

import javax.swing.OverlayLayout;


/**
 * OverlayLayoutInfo.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020527 nsano initial version <br>
 */
public class OverlayLayoutInfo extends SimpleBeanInfo {

    private final Class<?> clazz = OverlayLayout.class;
    private final Class<?> customizerClass = OverlayLayoutCustomizer.class;

    /** */
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, customizerClass);
    }

    /** */
    public Image getIcon(int iconKind) {
        return loadImage("resources/AbstractLayout.gif");
    }
}

/* */
