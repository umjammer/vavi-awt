/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.beans.SimpleBeanInfo;


/**
 * SimpleBorderInfo.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 */
public class SimpleBorderInfo extends SimpleBeanInfo
    implements BorderInfo {

    @Override
    public BorderPropertyDescriptor[] getBorderPropertyDescriptors() {
        return null;
    }
}
