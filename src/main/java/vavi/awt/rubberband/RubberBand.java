/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * ラバーバンドのモデルです．
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010829 nsano initial version <br>
 *          0.10 010903 nsano repackage <br>
 *          0.10 010904 nsano fix specifications??? <br>
 *          0.20 020509 nsano separate RubberBundSupport <br>
 *          0.30 020603 nsano make simple <br>
 *          0.21 020605 nsano fix specifications <br>
 */
public class RubberBand {

    /** 上にリサイズすることをあらわす定数 */
    public static final int RESIZE_N  = 1;
    /** 下にリサイズすることをあらわす定数 */
    public static final int RESIZE_S  = 2;
    /** 右にリサイズすることをあらわす定数 */
    public static final int RESIZE_E  = 4;
    /** 左にリサイズすることをあらわす定数 */
    public static final int RESIZE_W  = 8;
    /** 右上にリサイズすることをあらわす定数 */
    public static final int RESIZE_NE = 5;
    /** 左上にリサイズすることをあらわす定数 */
    public static final int RESIZE_NW = 9;
    /** 右下にリサイズすることをあらわす定数 */
    public static final int RESIZE_SE = 6;
    /** 左下にリサイズすることをあらわす定数 */
    public static final int RESIZE_SW = 10;
    /** 通常を表すの定数 */
    public static final int NORMAL_MODE = 0;
    /** 移動を表す定数 */
    public static final int MOVE_MODE = 3;

    /** RubberBnad のモード */
    protected int mode;

    /** リサイズするコンポーネント上におけるマウスが押されたときの座標 */
    protected Point resize;
    /** 移動するコンポーネント上におけるマウスが押されたときの座標 */
    protected Point move;
    /** 選択でプレスされたときの座標 */
    protected Point pressed;
    /** 領域選択でドラッグしている座標 */
    protected Point dragged;
    /** 領域選択でリリースされたときの座標 */
    protected Point released;

    /** 領域選択をしているかどうか (ドラッグ時 true, 離した時 false) */
    private boolean isSelecting;

    /** RubberBand を扱うコンテナ */
//  private Container container;

    /**
     * ラバーバンドを構築します．
     */
    public RubberBand() {
        mode = NORMAL_MODE;
        isSelecting = false;
    }

    /**
     * ラバーバンドのモードを設定します．
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /** ラバーバンドのモードを取得します． */
    public int getMode() {
        return mode;
    }

    //-------------------------------------------------------------------------

    /**
     * 選択中の Bounds を返します．
     */
    protected Rectangle getSelectionBounds(Point point) {
        isSelecting = true;

        dragged = point;

        int x = Math.min(pressed.x, dragged.x);
        int y = Math.min(pressed.y, dragged.y);
        int w = Math.abs(dragged.x - pressed.x);
        int h = Math.abs(dragged.y - pressed.y);

        return new Rectangle(x, y, w, h);
    }

    /**
     * 選択確定時の Bounds を返します．
     */
    protected Rectangle getSelectedBounds(Point point) {

        released = point;

        int x = Math.min(pressed.x, released.x);
        int y = Math.min(pressed.y, released.y);
        int w = x + Math.abs(released.x - pressed.x);
        int h = y + Math.abs(released.y - pressed.y);

        return new Rectangle(x, y, w, h);
    }

    /**
     * リサイズ中の相対 Bounds を返します．
     * TODO 大きさの制限
     */
    protected Rectangle getResizedBounds(Point point) {

        int x = 0;
        int y = 0;
        int w = 0;
        int h = 0;

        int dx = point.x - resize.x;
        int dy = point.y - resize.y;

        switch (mode) {
        case RESIZE_N:
            y += dy;
            h -= dy;
            break;
        case RESIZE_S:
            h += dy;
            break;
        case RESIZE_E:
            w += dx;
            break;
        case RESIZE_W:
            x += dx;
            w -= dx;
            break;
        case RESIZE_NE:
            y += dy;
            h -= dy;
            w += dx;
            break;
        case RESIZE_NW:
            y += dy;
            h -= dy;
            x += dx;
            w -= dx;
            break;
        case RESIZE_SE:
            h += dy;
            w += dx;
            break;
        case RESIZE_SW:
            h += dy;
            x += dx;
            w -= dx;
            break;
        default:
Debug.println(Level.SEVERE, "wrong resize mode: " + mode);
            break;
        }

        return new Rectangle(x, y, w, h);
    }

    /**
     * 移動中の相対 Point を返します．
     */
    protected Point getMovedPoint(Point point) {

        int x = point.x - move.x;
        int y = point.y - move.y;

        return new Point(x, y);
    }

    //-------------------------------------------------------------------------

    /**
     * ラバーバンドの開始を実行します．
     */
    public void start(Point point) {

        switch (mode) {
        case NORMAL_MODE:
            pressed = point;
            break;
        case MOVE_MODE:
            move = point;
// Debug.println(move.x + ", " + move.y);
            break;
        default:
            resize = point;
            break;
        }
    }

    /**
     * ラバーバンドの途中経過を実行します．
     */
    public void doing(Point point) {
        switch (mode) {
        case NORMAL_MODE: { // 領域選択
            Rectangle r = getSelectionBounds(point);
            fireSelecting(new RubberBandEvent(this, r));
            break;
        }
        case MOVE_MODE: { // 移動モード
            isSelecting = false;
            Point p = getMovedPoint(point);
// Debug.println(p.x + ", " + p.y);
            fireMoving(new RubberBandEvent(this, p));
            move = point;
            break;
        }
        default: { // リサイズ
            isSelecting = false;
            Rectangle r = getResizedBounds(point);
            fireResizing(new RubberBandEvent(this, r));
            resize = point;
            break;
        }}
    }

    /**
     * ラバーバンドの終了を実行します．
     */
    public void done(Point point) {
        switch (mode) {
        case NORMAL_MODE: { // 領域選択
            if (isSelecting) {
                Rectangle r = getSelectedBounds(point);
                fireSelected(new RubberBandEvent(this, r));
            }
            isSelecting = false;
            break;
        }
        case MOVE_MODE: { // 移動モードの処理
            Point p = getMovedPoint(point);
            fireMoved(new RubberBandEvent(this, p));
            break;
        }
        default: { // リサイズ処理
            Rectangle r = getResizedBounds(point);
            fireResized(new RubberBandEvent(this, r));
            break;
        }}
    }

    //-------------------------------------------------------------------------

    /** RubberBand イベント機構のサポート */
    private RubberBandSupport rbs = new RubberBandSupport();

    /** RubberBand リスナーを追加します． */
    public void addRubberBandListener(RubberBandListener l) {
        rbs.addRubberBandListener(l);
    }

    /** RubberBand リスナーを削除します． */
    public void removeRubberBandListener(RubberBandListener l) {
        rbs.removeRubberBandListener(l);
    }

    /** 選択中のイベントを発行します． */
    public void fireSelecting(RubberBandEvent ev) {
        rbs.fireSelecting(ev);
    }

    /** 選択確定のイベントを発行します． */
    public void fireSelected(RubberBandEvent ev) {
        rbs.fireSelected(ev);
    }

    /** 選択対象が移動中のイベントを発行します． */
    public void fireMoving(RubberBandEvent ev) {
        rbs.fireMoving(ev);
    }

    /** 選択対象が移動確定のイベントを発行します． */
    public void fireMoved(RubberBandEvent ev) {
        rbs.fireMoved(ev);
    }

    /** 選択対象がリサイズ中のイベントを発行します． */
    public void fireResizing(RubberBandEvent ev) {
        rbs.fireResizing(ev);
    }

    /** 選択対象がリサイズ確定のイベントを発行します． */
    public void fireResized(RubberBandEvent ev) {
        rbs.fireResized(ev);
    }
}

/* */
