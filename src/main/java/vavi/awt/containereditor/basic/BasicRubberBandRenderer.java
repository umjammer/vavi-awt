/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.containereditor.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;

import vavi.awt.rubberband.RubberBand;
import vavi.awt.rubberband.RubberBandRenderer;


/**
 * Basic ラバーバンドレンダラです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010829 nsano initial version <br>
 *          0.10 020611 nsano fix <br>
 *          0.11 020613 nsano refine <br>
 */
public class BasicRubberBandRenderer implements RubberBandRenderer {

    /** */
    private Container container;

    /** */
    public BasicRubberBandRenderer(Container container) {
        this.container = container;
    }

    /** リサイズの幅 */
    private Insets resizingInsets = new Insets(5, 5, 5, 5);

    /** カーソルのテーブル TODO */
    private static final Cursor[] cursors = {
        Cursor.getDefaultCursor(),
        Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR),  // 1
        Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR),  // 2
        Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR),
        Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR),  // 4
        Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR), // 5
        Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR), // 6
        null,
        Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR),  // 8
        Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR), // 9
        Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)  // 10
    };

    /** A rubber band selector. */
    private final JComponent selector = new JComponent() {
        /** Draws red rubber band. */
        @Override public void paintComponent(Graphics g) {
            g.setColor(Color.red);
            g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        }
    };

    @Override
    public void drawSelecting(Rectangle bounds) {
        if (selector.getParent() == null) {
            container.add(selector, 0);
            selector.setVisible(true);
        }
        selector.setBounds(bounds);
    }

    @Override
    public void drawSelected(Rectangle bounds) {
        selector.setVisible(false);
        container.remove(selector);
    }

    @Override
    public void drawMoving(Rectangle r) {
    }

    @Override
    public void drawMoved(Rectangle r) {
    }

    @Override
    public void drawResizing(Rectangle r) {
    }

    @Override
    public void drawResized(Rectangle r) {
    }

    @Override
    public int getMode(Component component, Point point) {
        int mode;

        int x = point.x;
        int y = point.y;

        Insets ri = resizingInsets;

        int minX = ri.left;
        int maxX = -ri.right + component.getSize().width;
        int minY = ri.top;
        int maxY = -ri.bottom + component.getSize().height;

        if (x < minX && y < minY) { // resize northwest
            mode = RubberBand.RESIZE_NW;
        } else if (x < minX && y > maxY) { // resize southwest
            mode = RubberBand.RESIZE_SW;
        } else if (x > maxX && y < minY) { // resize northeast
            mode = RubberBand.RESIZE_NE;
        } else if (x > maxX && y > maxY) { // resize southeast
            mode = RubberBand.RESIZE_SE;
        } else if ((x > minX && x < maxX) && y < minY) { // resize north
            mode = RubberBand.RESIZE_N;
        } else if ((x > minX && x < maxX) && y > maxY) { // resize south
            mode = RubberBand.RESIZE_S;
        } else if (x < minX && (y > minY && y < maxY)) { // resize west
            mode = RubberBand.RESIZE_W;
        } else if (x > maxX && (y > minY && y < maxY)) { // resize east
            mode = RubberBand.RESIZE_E;
        } else { // moving mode
            mode = RubberBand.MOVE_MODE;
        }

        return mode;
    }

    @Override
    public Cursor getCursor(int mode) {
        return cursors[mode];
    }
}

/* */
