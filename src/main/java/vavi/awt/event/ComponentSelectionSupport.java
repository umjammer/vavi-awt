/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.io.Serializable;

import javax.swing.event.EventListenerList;


/**
 * ComponentSelection リスナーのユーティリティです．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020509 nsano initial version <br>
 */
public class ComponentSelectionSupport implements Serializable {

    /** ComponentSelection のリスナー */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * ComponentSelection リスナーをアタッチします．
     * 
     * @param l ComponentSelectionListener
     */
    public void addComponentSelectionListener(ComponentSelectionListener l) {
        listenerList.add(ComponentSelectionListener.class, l);
    }

    /**
     * ComponentSelection リスナーをリムーブします．
     * 
     * @param l ComponentSelectionListener
     */
    public void removeComponentSelectionListener(ComponentSelectionListener l) {
        listenerList.remove(ComponentSelectionListener.class, l);
    }

    /**
     * ComponentSelection イベントを発行します．
     */
    public void fireValueChanged(ComponentSelectionEvent ev) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ComponentSelectionListener.class) {
                ((ComponentSelectionListener) listeners[i + 1]).valueChanged(ev);
            }
        }
    }
}

/* */
