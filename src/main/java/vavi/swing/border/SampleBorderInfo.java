/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.border.Border;


/**
 * SampleBorderInfo.
 * <p>
 * setters are for {@link java.beans.XMLDecoder}, see {@code border.xml}.
 * </p>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020525 nsano initial version <br>
 */
public class SampleBorderInfo /* extends SimpleBeanInfo
    implements BorderInfo  */ {

    public Border border;
    public String desc;
    public Icon icon;

    /** */
    public void setBorder(Border border) {
        this.border = border;
    }

    /** */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /** @param name resource name relative to this class */
    public void setIconResource(String name) {
        this.icon = new ImageIcon(SampleBorderInfo.class.getResource(name));
    }

//      public BorderPropertyDescriptor[] getBorderPropertyDescriptors() {
//          return null;
//      }
}
