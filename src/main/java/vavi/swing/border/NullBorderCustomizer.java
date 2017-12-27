/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import javax.swing.border.Border;


/**
 * NullBorderCustomizer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class NullBorderCustomizer extends BasicBorderCustomizer {

    /** */
    public NullBorderCustomizer() {
    }

    /** */
    public void setObject(Border border) {
        borderSample.setBorder(null);

        Border oldBorder = this.border;
        this.border = null;

        firePropertyChange("border", oldBorder, border);
    }
}

/* */
