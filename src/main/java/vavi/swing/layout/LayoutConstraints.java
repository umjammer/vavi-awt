/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.beans.*;


/**
 * LThis is a base class for creating wrapper classes
 * that handle the ayoutManager's Constraints class as Beans.
 * <p>
 * setter The method should issue a PropertyChangeEvent. // TODO
 * </p>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 */
public abstract class LayoutConstraints {

    /**
     * Gets constraints.
     */
    public abstract Object getConstraints();

    /**
     * Sets constraints.
     */
    public abstract void setConstraints(Object constraints);

    // ----

    /** PropertyChange event mechanism utilities */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /** Add a PropertyChangeListener. */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /** Removes a PropertyChangeListener. */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /** Emits a PropertyChangeEvent. */
    protected void firePropertyChange(String name,
                                      int oldValue, int newValue) {
        pcs.firePropertyChange(name, oldValue, newValue);
    }

    /** Emits a PropertyChangeEvent. */
    protected void firePropertyChange(String name,
                                      Object oldValue, Object newValue) {
        pcs.firePropertyChange(name, oldValue, newValue);
    }
}
