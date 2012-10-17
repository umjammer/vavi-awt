/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;


/**
 * ラバーバンドレンダラのインターフェースです．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010829 nsano initial version <br>
 *          0.01 010903 nsano repackage <br>
 *          0.10 010904 nsano fix specifications??? <br>
 *          0.11 020509 nsano add getXXXInset <br>
 *          0.12 020604 nsano remove getXXXInset <br>
 *          1.00 020606 nsano fix specification <br>
 */
public interface RubberBandRenderer {

    /**
     * 選択時のラバーバンドを描きます．
     */
    void drawSelecting(Rectangle bounds);

    /**
     * 選択後のラバーバンドを描画します．
     */
    void drawSelected(Rectangle bounds);

    /**
     * 移動時のラバーバンドを描画します．
     */
    void drawMoving(Rectangle bounds);

    /**
     * 移動後のラバーバンドを描画します．
     */
    void drawMoved(Rectangle bounds);

    /**
     * リサイズ時のラバーバンドを描画します．
     */
    void drawResizing(Rectangle bounds);

    /**
     * リサイズ後のラバーバンドを描画します．
     */
    void drawResized(Rectangle bounds);

    /** */
    int getMode(Component component, Point point);

    /** */
    Cursor getCursor(int mode);
}

/* */
