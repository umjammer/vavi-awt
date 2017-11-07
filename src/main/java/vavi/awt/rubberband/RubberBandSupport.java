/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;

import java.io.Serializable;

import javax.swing.event.EventListenerList;


/**
 * RubberBand イベント機構のユーティリティです．
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010904 nsano initial version <br>
 *          0.10 010904 nsano fix specifications??? <br>
 *          0.20 020503 nsano use EventListenerList <br>
 */
public class RubberBandSupport
    implements Serializable {

    /** RubberBand のリスナー */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * RubberBand リスナーを追加します．
     * @param l RubberBandListener
     */
    public void addRubberBandListener(RubberBandListener l) {
        listenerList.add(RubberBandListener.class, l);
    }

    /**
     * RubberBand リスナーを削除します．
     * @param l RubberBandListener
     */
    public void removeRubberBandListener(RubberBandListener l) {
        listenerList.remove(RubberBandListener.class, l);
    }

    /**
     * 選択中のイベントを発行します．
     */
    public void fireSelecting(RubberBandEvent ev) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == RubberBandListener.class) {
                ((RubberBandListener) listeners[i + 1]).selecting(ev);
            }
        }
    }

    /**
     * 選択確定のイベントを発行します．
     */
    public void fireSelected(RubberBandEvent ev) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == RubberBandListener.class) {
                ((RubberBandListener) listeners[i + 1]).selected(ev);
            }
        }
    }

    /**
     * 選択対象が移動中のイベントを発行します．
     */
    public void fireMoving(RubberBandEvent ev) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == RubberBandListener.class) {
                ((RubberBandListener) listeners[i + 1]).moving(ev);
            }
        }
    }

    /**
     * 選択対象が移動確定のイベントを発行します．
     */
    public void fireMoved(RubberBandEvent ev) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == RubberBandListener.class) {
                ((RubberBandListener) listeners[i + 1]).moved(ev);
            }
        }
    }

    /**
     * 選択対象がリサイズ中のイベントを発行します．
     */
    public void fireResizing(RubberBandEvent ev) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == RubberBandListener.class) {
                ((RubberBandListener) listeners[i + 1]).resizing(ev);
            }
        }
    }

    /**
     * 選択対象がリサイズ確定のイベントを発行します．
     */
    public void fireResized(RubberBandEvent ev) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == RubberBandListener.class) {
                ((RubberBandListener) listeners[i + 1]).resized(ev);
            }
        }
    }
}

/* */
