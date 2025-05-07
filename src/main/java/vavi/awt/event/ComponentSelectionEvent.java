/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.util.EventObject;


/**
 * The class for the event that is fired when a Component is selected.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020509 nsano initial version <br>
 */
public class ComponentSelectionEvent extends EventObject {

    /** */
    private final Object selected;

    /**
     * Constructs a ComponentSelection event.
     */
    public ComponentSelectionEvent(Object source, Object selected) {
        super(source);

        this.selected = selected;
    }

    /** */
    public Object getSelected() {
        return selected;
    }
}
