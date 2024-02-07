/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A utility class for a RubberBand event.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010904 nsano initial version <br>
 *          0.10 010904 nsano fix specifications??? <br>
 *          0.20 020503 nsano use EventListenerList <br>
 */
public class RubberBandSupport implements Serializable {

    /** RubberBand listeners */
    private final List<RubberBandListener> listeners = new ArrayList<>();

    /**
     * Adds a RubberBand listener.
     * @param l RubberBandListener
     */
    public void addRubberBandListener(RubberBandListener l) {
        listeners.add(l);
    }

    /**
     * Removes a RubberBand listener.
     * @param l RubberBandListener
     */
    public void removeRubberBandListener(RubberBandListener l) {
        listeners.remove(l);
    }

    /**
     * Fires a selecting event.
     */
    public void fireSelecting(RubberBandEvent ev) {
        for (RubberBandListener listener : listeners) {
            listener.selecting(ev);
        }
    }

    /**
     * Fires a selected event.
     */
    public void fireSelected(RubberBandEvent ev) {
        for (RubberBandListener listener : listeners) {
            listener.selected(ev);
        }
    }

    /**
     * Fires a moving event.
     */
    public void fireMoving(RubberBandEvent ev) {
        for (RubberBandListener listener : listeners) {
            listener.moving(ev);
        }
    }

    /**
     * Fires a moved event.
     */
    public void fireMoved(RubberBandEvent ev) {
        for (RubberBandListener listener : listeners) {
            listener.moved(ev);
        }
    }

    /**
     * Fires a resizing event.
     */
    public void fireResizing(RubberBandEvent ev) {
        for (RubberBandListener listener : listeners) {
            listener.resizing(ev);
        }
    }

    /**
     * Fires a resized event.
     */
    public void fireResized(RubberBandEvent ev) {
        for (RubberBandListener listener : listeners) {
            listener.resized(ev);
        }
    }
}

/* */
