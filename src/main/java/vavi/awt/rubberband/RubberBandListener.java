/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;

import java.util.EventListener;


/**
 * A RubberBand event listener.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010904 nsano initial version <br>
 */
public interface RubberBandListener extends EventListener {

    /** when selecting */
    void selecting(RubberBandEvent ev);

    /** when selecting finished */
    void selected(RubberBandEvent ev);

    /**
     * when moving.
     */
    void moving(RubberBandEvent ev);

    /**
     * when moving finished.
     */
    void moved(RubberBandEvent ev);

    /**
     * when resizing.
     */
    void resizing(RubberBandEvent ev);

    /**
     * when resizing finished.
     */
    void resized(RubberBandEvent ev);
}

/* */
