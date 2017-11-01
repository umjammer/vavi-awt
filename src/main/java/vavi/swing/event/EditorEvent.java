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
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.01 020503 nsano repackage <br>
 *          0.02 020510 nsano repackage <br>
 */
public class EditorEvent extends EventObject {

    /** */
    private String name;

    /** */
    private Object argument;

    /**
     * Creates an editor event.
     * @param    source    the event source
     */
//  public EditorEvent(Object source) {
//      this(source, "", null);
//  }

    /**
     * Creates an editor event.
     * 
     * @param source the event source
     * @param name the event name
     */
    public EditorEvent(Object source, String name) {
        this(source, name, null);
    }

    /**
     * Creates an editor event.
     * 
     * @param source the event source
     * @param name the event name
     * @param argument the event argument
     */
    public EditorEvent(Object source, String name, Object argument) {
        super(source);

        this.name = name;
        this.argument = argument;
    }

    /** */
    public String getName() {
        return name;
    }

    /** @return nullable */
    public Object getArgument() {
        return argument;
    }
}

/* */
