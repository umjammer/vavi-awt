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
 * ComponentSelection リスナーのユーティリティです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020509 nsano initial version <br>
 */
public class ComponentSelectionSupport implements Serializable {

    /** ComponentSelection のリスナー */
    private List<ComponentSelectionListener> listeners = new ArrayList<>();

    /**
     * ComponentSelection リスナーをアタッチします．
     *
     * @param l ComponentSelectionListener
     */
    public void addComponentSelectionListener(ComponentSelectionListener l) {
        listeners.add(l);
    }

    /**
     * ComponentSelection リスナーをリムーブします．
     *
     * @param l ComponentSelectionListener
     */
    public void removeComponentSelectionListener(ComponentSelectionListener l) {
        listeners.remove(l);
    }

    /**
     * ComponentSelection イベントを発行します．
     */
    public void fireValueChanged(ComponentSelectionEvent ev) {
        for (ComponentSelectionListener listener : listeners) {
            listener.valueChanged(ev);
        }
    }
}

/* */
