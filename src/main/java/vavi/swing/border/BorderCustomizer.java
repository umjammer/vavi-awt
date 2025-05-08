/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.beans.PropertyChangeListener;

import javax.swing.border.Border;


/**
 * BorderCustomizer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 *          1.00 020527 nsano complete <br>
 */
public interface BorderCustomizer /* extends Customizer */ {

    /**
     * Registers a listener for PropertyChange events.
     * @param listener The object that is called when the PropertyChange event is triggered.
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes a listener for the PropertyChange event.
     * @param listener The PropertyChange listener to be removed.
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Set the Border you want to customize.
     * This method can only be called once before the Customizer is added to a parent AWT container.
     */
    void setObject(Border border);

    /** TODO Only for TabbedPane's ChangeEvent */
    Border getObject();
}
