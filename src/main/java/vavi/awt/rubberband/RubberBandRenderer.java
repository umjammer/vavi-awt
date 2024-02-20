/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;


/**
 * Represents a rubber band renderer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010829 nsano initial version <br>
 *          0.01 010903 nsano repackage <br>
 *          0.10 010904 nsano fix specifications??? <br>
 *          0.11 020509 nsano add getXXXInset <br>
 *          0.12 020604 nsano remove getXXXInset <br>
 *          1.00 020606 nsano fix specification <br>
 */
public interface RubberBandRenderer {

    /**
     * Draws rubber band when selecting.
     */
    void drawSelecting(Rectangle bounds);

    /**
     * Draws rubber band when selected.
     */
    void drawSelected(Rectangle bounds);

    /**
     * Draws rubber band when moving.
     */
    void drawMoving(Rectangle bounds);

    /**
     * Draws rubber band when moved.
     */
    void drawMoved(Rectangle bounds);

    /**
     * Draws rubber band when resizing.
     */
    void drawResizing(Rectangle bounds);

    /**
     * Draws rubber band when resized.
     */
    void drawResized(Rectangle bounds);

    /** Gets mode */
    int getMode(Component component, Point point);

    /**
     * @see RubberBand#getMode()
     * @param mode RubberBand#MOVE_MODE
     */
    Cursor getCursor(int mode);
}

/* */
