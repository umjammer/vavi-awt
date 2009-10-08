/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;


/**
 * RubberBandAdapter.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020604 nsano initial version <br>
 */
public abstract class RubberBandAdapter implements RubberBandListener {

    /** 選択中に呼ばれます． */
    public void selecting(RubberBandEvent ev) {
    }

    /** 選択確定時に呼ばれます． */
    public void selected(RubberBandEvent ev) {
    }

    /** 選択対象の移動中に呼ばれます． */
    public void moving(RubberBandEvent ev) {
    }

    /** 選択対象の移動確定時に呼ばれます． */
    public void moved(RubberBandEvent ev) {
    }

    /** 選択対象のリサイズ中に呼ばれます． */
    public void resizing(RubberBandEvent ev) {
    }

    /** 選択対象のリサイズ確定時に呼ばれます． */
    public void resized(RubberBandEvent ev) {
    }
}

/* */
