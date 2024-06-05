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
 * Represents rubber band model.．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010829 nsano initial version <br>
 *          0.10 010903 nsano repackage <br>
 *          0.10 010904 nsano fix specifications??? <br>
 *          0.20 020509 nsano separate RubberBandSupport <br>
 *          0.30 020603 nsano make simple <br>
 *          0.21 020605 nsano fix specifications <br>
 */
public class RubberBand {

    /** resizing to north */
    public static final int RESIZE_N  = 1;
    /** resizing to south */
    public static final int RESIZE_S  = 2;
    /** resizing to east */
    public static final int RESIZE_E  = 4;
    /** resizing to west */
    public static final int RESIZE_W  = 8;
    /** resizing to northeast */
    public static final int RESIZE_NE = 5;
    /** resizing to northwest */
    public static final int RESIZE_NW = 9;
    /** resizing to southeast */
    public static final int RESIZE_SE = 6;
    /** resizing to southwest */
    public static final int RESIZE_SW = 10;
    /** normal */
    public static final int NORMAL_MODE = 0;
    /** moving */
    public static final int MOVE_MODE = 3;

    /**
     * mode of RubberBand
     * @see #NORMAL_MODE
     * @see #MOVE_MODE
     */
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

//    /** RubberBand を扱うコンテナ */
//  private Container container;

    /**
     * ラバーバンドを構築します．
     */
    public RubberBand() {
        mode = NORMAL_MODE;
        isSelecting = false;
    }

    /**
     * Sets rubber band mode.．
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /** Gets rubber band mode */
    public int getMode() {
        return mode;
    }

    //-------------------------------------------------------------------------

    /**
     * Gets Bounds selecting．
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
     * Gets bounds selected.
     */
    protected Rectangle getSelectedBounds(Point point) {

        released = point;

        int x = Math.min(pressed.x, released.x);
        int y = Math.min(pressed.y, released.y);
        int w = Math.abs(released.x - pressed.x);
        int h = Math.abs(released.y - pressed.y);

        return new Rectangle(x, y, w, h);
    }

    /**
     * Gets relative bounds when resizing.
     * TODO size limit
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
     * Gets relative Point.
     */
    protected Point getMovedPoint(Point point) {

        int x = point.x - move.x;
        int y = point.y - move.y;

        return new Point(x, y);
    }

    //-------------------------------------------------------------------------

    /**
     * Rubber band starts processing.
     */
    public void start(Point point) {

        switch (mode) {
        case NORMAL_MODE:
            pressed = point;
            break;
        case MOVE_MODE:
            move = point;
Debug.println(Level.FINER, move.x + ", " + move.y);
            break;
        default:
            resize = point;
            break;
        }
    }

    /**
     * Rubber band is now processing.
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
Debug.println(Level.FINER, p.x + ", " + p.y);
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
     * Rubber band processing is done.
     */
    public void done(Point point) {
        switch (mode) {
        case NORMAL_MODE: { // selecting area
            if (isSelecting) {
                Rectangle r = getSelectedBounds(point);
                fireSelected(new RubberBandEvent(this, r));
            }
            isSelecting = false;
            break;
        }
        case MOVE_MODE: { // moving mode
            Point p = getMovedPoint(point);
            fireMoved(new RubberBandEvent(this, p));
            break;
        }
        default: { // resizing
            Rectangle r = getResizedBounds(point);
            fireResized(new RubberBandEvent(this, r));
            break;
        }}
    }

    //-------------------------------------------------------------------------

    /** RubberBand event support */
    private final RubberBandSupport rbs = new RubberBandSupport();

    /** Adds a RubberBand listener. */
    public void addRubberBandListener(RubberBandListener l) {
        rbs.addRubberBandListener(l);
    }

    /** Removes a RubberBand listener/ */
    public void removeRubberBandListener(RubberBandListener l) {
        rbs.removeRubberBandListener(l);
    }

    /** Fires a selecting event. */
    public void fireSelecting(RubberBandEvent ev) {
        rbs.fireSelecting(ev);
    }

    /** Fires a selected event. */
    public void fireSelected(RubberBandEvent ev) {
        rbs.fireSelected(ev);
    }

    /** Fires a moving event. */
    public void fireMoving(RubberBandEvent ev) {
        rbs.fireMoving(ev);
    }

    /** Fires a moved event. */
    public void fireMoved(RubberBandEvent ev) {
        rbs.fireMoved(ev);
    }

    /** Fires a resizing event. */
    public void fireResizing(RubberBandEvent ev) {
        rbs.fireResizing(ev);
    }

    /** Fires a resized event. */
    public void fireResized(RubberBandEvent ev) {
        rbs.fireResized(ev);
    }
}
