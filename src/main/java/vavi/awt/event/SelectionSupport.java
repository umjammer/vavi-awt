/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.io.Serializable;

import javax.swing.event.EventListenerList;


/**
 * Selection リスナーのユーティリティです．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020603 nsano initial version <br>
 */
public class SelectionSupport implements Serializable {

    /** Selection のリスナー */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * Selection リスナーをアタッチします．
     * 
     * @param l SelectionListener
     */
    public void addSelectionListener(SelectionListener l) {
        listenerList.add(SelectionListener.class, l);
    }

    /**
     * Selection リスナーをリムーブします．
     * 
     * @param l SelectionListener
     */
    public void removeSelectionListener(SelectionListener l) {
        listenerList.remove(SelectionListener.class, l);
    }

    /**
     * Selection イベントを発行します．
     */
    public void fireValueChanged(SelectionEvent ev) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SelectionListener.class) {
                ((SelectionListener) listeners[i + 1]).valueChanged(ev);
            }
        }
    }
}

/* */
