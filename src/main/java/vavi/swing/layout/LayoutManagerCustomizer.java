/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Container;
import java.awt.LayoutManager;
import java.beans.PropertyChangeListener;


/**
 * LayoutManagerCustomizer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 */
public interface LayoutManagerCustomizer {

    /**
     * PropertyChange Register a listener for an event.
     * @param listener PropertyChange The object that will be called when the event is triggered.
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * PropertyChange Removes a listener for an event.
     * @param listener The PropertyChange listener to be removed.
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Set the LayoutManager you want to customize.
     * This method can only be called once before the Customizer is added to a parent AWT container.
     */
    void setObject(LayoutManager layout);

    /** TODO */
    LayoutManager getObject();

    /** */
    void setContainer(Container container);

    /** */
    void layoutContainer();
}
