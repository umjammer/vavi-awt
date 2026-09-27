/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.LayoutManager;

import javax.swing.Icon;
import javax.swing.ImageIcon;


/**
 * SampleLayoutManagerInfo.
 * <p>
 * setters are for {@link java.beans.XMLDecoder}, see {@code layoutManager.xml}.
 * </p>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 */
public class SampleLayoutManagerInfo /* extends SimpleBeanInfo */ {

    /** */
    public LayoutManager layout;
    /** */
    public String desc;
    /** */
    public Icon icon;

    /** */
    public void setLayout(LayoutManager layout) {
        this.layout = layout;
    }

    /** */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /** @param name resource name relative to this class */
    public void setIconResource(String name) {
        this.icon = new ImageIcon(SampleLayoutManagerInfo.class.getResource(name));
    }
}
