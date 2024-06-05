/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Selection リスナーのユーティリティです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020603 nsano initial version <br>
 */
public class SelectionSupport implements Serializable {

    /** Selection のリスナー */
    private List<SelectionListener> listenerList = new ArrayList<>();

    /**
     * Selection リスナーをアタッチします．
     *
     * @param l SelectionListener
     */
    public void addSelectionListener(SelectionListener l) {
        listenerList.add(l);
    }

    /**
     * Selection リスナーをリムーブします．
     *
     * @param l SelectionListener
     */
    public void removeSelectionListener(SelectionListener l) {
        listenerList.remove(l);
    }

    /**
     * Selection イベントを発行します．
     */
    public void fireValueChanged(SelectionEvent ev) {
        for (SelectionListener listener : listenerList) {
            listener.valueChanged(ev);
        }
    }
}
