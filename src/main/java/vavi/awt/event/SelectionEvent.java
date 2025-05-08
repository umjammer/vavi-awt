/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.util.EventObject;


/**
 * The class for events that are fired when an Object is selected.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020603 nsano initial version <br>
 */
public class SelectionEvent extends EventObject {

    /** */
    private final Object selected;

    /**
     * Constructs a Selection event.
     */
    public SelectionEvent(Object source, Object selected) {
        super(source);

        this.selected = selected;
    }

    /** */
    public Object getSelected() {
        return selected;
    }
}
