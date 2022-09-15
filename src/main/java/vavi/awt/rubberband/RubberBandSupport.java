/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * RubberBand イベント機構のユーティリティです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010904 nsano initial version <br>
 *          0.10 010904 nsano fix specifications??? <br>
 *          0.20 020503 nsano use EventListenerList <br>
 */
public class RubberBandSupport implements Serializable {

    /** RubberBand のリスナー */
    private List<RubberBandListener> listeners = new ArrayList<>();

    /**
     * RubberBand リスナーを追加します．
     * @param l RubberBandListener
     */
    public void addRubberBandListener(RubberBandListener l) {
        listeners.add(l);
    }

    /**
     * RubberBand リスナーを削除します．
     * @param l RubberBandListener
     */
    public void removeRubberBandListener(RubberBandListener l) {
        listeners.remove(l);
    }

    /**
     * 選択中のイベントを発行します．
     */
    public void fireSelecting(RubberBandEvent ev) {
        for (RubberBandListener listener : listeners) {
            listener.selecting(ev);
        }
    }

    /**
     * 選択確定のイベントを発行します．
     */
    public void fireSelected(RubberBandEvent ev) {
        for (RubberBandListener listener : listeners) {
            listener.selected(ev);
        }
    }

    /**
     * 選択対象が移動中のイベントを発行します．
     */
    public void fireMoving(RubberBandEvent ev) {
        for (RubberBandListener listener : listeners) {
            listener.moving(ev);
        }
    }

    /**
     * 選択対象が移動確定のイベントを発行します．
     */
    public void fireMoved(RubberBandEvent ev) {
        for (RubberBandListener listener : listeners) {
            listener.moved(ev);
        }
    }

    /**
     * 選択対象がリサイズ中のイベントを発行します．
     */
    public void fireResizing(RubberBandEvent ev) {
        for (RubberBandListener listener : listeners) {
            listener.resizing(ev);
        }
    }

    /**
     * 選択対象がリサイズ確定のイベントを発行します．
     */
    public void fireResized(RubberBandEvent ev) {
        for (RubberBandListener listener : listeners) {
            listener.resized(ev);
        }
    }
}

/* */
