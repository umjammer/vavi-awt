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
 * {@link vavi.swing.JFileChooserHistoryComboBox} の BeanInfo です．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010917 nsano initial version <br>
 *          1.00 020515 nsano fix <br>
 */
public class JFileChooserHistoryComboBoxBeanInfo extends SimpleBeanInfo {

    /** */
    private static final String path = "/vavi/swing/beaninfo/resources/";

    /** */
    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16) {
            Image image = loadImage(path + "FileChooser16.gif");
            return image;
        }
        return null;
    }
}
