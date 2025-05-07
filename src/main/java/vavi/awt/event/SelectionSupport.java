/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Selection listener utility.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020603 nsano initial version <br>
 */
public class SelectionSupport implements Serializable {

    /** Selection listeners */
    private final List<SelectionListener> listenerList = new ArrayList<>();

    /**
     * Attach a Selection listener.
     *
     * @param l SelectionListener
     */
    public void addSelectionListener(SelectionListener l) {
        listenerList.add(l);
    }

    /**
     * Removes the Selection listener.
     *
     * @param l SelectionListener
     */
    public void removeSelectionListener(SelectionListener l) {
        listenerList.remove(l);
    }

    /**
     * Selection event is issued.
     */
    public void fireValueChanged(SelectionEvent ev) {
        for (SelectionListener listener : listenerList) {
            listener.valueChanged(ev);
        }
    }
}
