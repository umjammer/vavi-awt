/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;


/**
 * {@link vavi.swing.JVolume} の BeanInfo です．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020516 nsano initial version <br>
 */
public class JVolumeBeanInfo extends SimpleBeanInfo {

    /** */
    private static final String path = "/vavi/swing/beaninfo/resources/";

    /** */
    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16) {
            Image image = loadImage(path + "JVolume16.png");
            return image;
        }
        return null;
    }
}

/* */
