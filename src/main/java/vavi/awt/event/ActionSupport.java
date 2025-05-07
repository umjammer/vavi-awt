/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Vector;


/**
 * Action listener utilities.
 *
 * @target 1.1
 * @caution Don't use swing because it's written for JDK 1.1!
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020503 nsano initial version <br>
 *          0.01 020914 nsano TODO synchronized <br>
 */
public class ActionSupport implements Serializable {

    /** Action listeners */
    private final Vector<ActionListener> listeners = new Vector<>();

    /**
     * Attaches an action listener.
     *
     * @param l ActionListener
     */
    public void addActionListener(ActionListener l) {
        listeners.addElement(l);
    }

    /**
     * Removes an action listener.
     *
     * @param l ActionListener
     */
    public void removeActionListener(ActionListener l) {
        listeners.removeElement(l);
    }

    /**
     * Issues an event.
     */
    public synchronized void fireActionPerformed(ActionEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.elementAt(i).actionPerformed(ev);
        }
    }
}
