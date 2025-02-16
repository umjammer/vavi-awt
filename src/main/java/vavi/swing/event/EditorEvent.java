/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.event;

import java.util.EventObject;


/**
 * エディタのイベントです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.01 020503 nsano repackage <br>
 *          0.02 020510 nsano repackage <br>
 */
public class EditorEvent extends EventObject {

    /** */
    private final String name;

    /** */
    private final Object[] arguments;

    /**
     * Creates an editor event.
     *
     * @param source the event source
     * @param name the event name
     */
    public EditorEvent(Object source, String name) {
        this(source, name, (Object) null);
    }

    /**
     * Creates an editor event.
     *
     * @param source the event source
     * @param name the event name
     * @param arguments the event argument
     */
    public EditorEvent(Object source, String name, Object... arguments) {
        super(source);

        this.name = name;
        this.arguments = arguments;
    }

    /** */
    public String getName() {
        return name;
    }

    /** @return nullable */
    public Object[] getArguments() {
        return arguments;
    }
}
