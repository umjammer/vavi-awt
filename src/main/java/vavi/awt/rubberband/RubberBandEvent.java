/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.EventObject;


/**
 * This is the class for events that are fired when a RubberBand is changed.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010904 nsano initial version <br>
 *          0.10 010904 nsano fix specifications??? <br>
 *          0.20 020604 nsano fix specifications??? <br>
 *          0.21 020605 nsano fix specifications <br>
 */
public class RubberBandEvent extends EventObject {

    /** Component relative location */
    private Point location;
//    /** The relative size of the component */
//  private Dimension size;
    /** Selected Area */
    private Rectangle bounds;

    /**
     * Constructs a RubberBand event.
     * @see RubberBandListener#selecting
     * @see RubberBandListener#selected
     */
    public RubberBandEvent(Object source, Rectangle bounds) {
        super(source);
        this.bounds = bounds;
    }

    /**
     * Constructs a RubberBand event.
     * @see RubberBandListener#moving
     * @see RubberBandListener#moved
     * @param location Component relative location
     */
    public RubberBandEvent(Object source, Point location) {
        super(source);
        this.location = location;
    }

//    /**
//     * Constructs a RubberBand event.
//     * @see RubberBandListener#resizing
//     * @see RubberBandListener#resized
//     * @param size The relative size of the component
//     */
//  public RubberBandEvent(Object source, Dimension size) {
//      super(source);
//      this.size = size;
//  }

    /** Gets the selected region. */
    public Rectangle getBounds() {
        return bounds;
    }

    /** Gets the relative location of the component. */
    public Point getLocation() {
        return location;
    }

//    /** Gets the relative size of the component. */
//  public Dimension getDimension() {
//      return size;
//  }
}
