/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.fontchooser;

import java.awt.Component;
import java.awt.Font;


/**
 * FontChooser interface
 *
 * @author	<a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version	0.00	020517	nsano	initial version <br>
 */
public interface FontEditor {

    /** TODO */
    Component getFontEditorComponent();

    /** TODO */
    void setSelectedFont(Font font);

    /** TODO */
    Font getSelectedFont();
}

/* */
