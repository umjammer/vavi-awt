/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.util.EventListener;


/**
 * ComponentSelection listener interface.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020509 nsano initial version <br>
 */
public interface ComponentSelectionListener extends EventListener {

    /**
     * Change the attributes.
     */
    void valueChanged(ComponentSelectionEvent ev);
}
