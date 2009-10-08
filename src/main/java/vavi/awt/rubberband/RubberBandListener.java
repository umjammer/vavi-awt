/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;

import java.util.EventListener;


/**
 * RubberBand のリスナーインターフェースです．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010904 nsano initial version <br>
 */
public interface RubberBandListener extends EventListener {

    /**
     * 選択中に呼ばれます．
     */
    void selecting(RubberBandEvent ev);

    /**
     * 選択確定時に呼ばれます．
     */
    void selected(RubberBandEvent ev);

    /**
     * 選択対象の移動中に呼ばれます．
     */
    void moving(RubberBandEvent ev);

    /**
     * 選択対象の移動確定時に呼ばれます．
     */
    void moved(RubberBandEvent ev);

    /**
     * 選択対象のリサイズ中に呼ばれます．
     */
    void resizing(RubberBandEvent ev);

    /**
     * 選択対象のリサイズ確定時に呼ばれます．
     */
    void resized(RubberBandEvent ev);
}

/* */
