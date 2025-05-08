/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.util.EventListener;


/**
 * Selection listener interface.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020603 nsano initial version <br>
 */
public interface SelectionListener extends EventListener {

    /**
     * Change the attributes.
     */
    void valueChanged(SelectionEvent ev);
}
