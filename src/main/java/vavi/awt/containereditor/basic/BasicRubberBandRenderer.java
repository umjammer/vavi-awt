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
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
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
    private Cursor[] cursors = {
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

    /** ラバーバンドセレクタ */
    private JComponent selector = new JComponent() {
        /** 赤い選択用ラバーバンド描画 */
        public void paintComponent(Graphics g) {
            g.setColor(Color.red);
            g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        }
    };

    /** ラバーバンドセレクタ表示，追加，サイズ変更． */
    public void drawSelecting(Rectangle bounds) {
        if (selector.getParent() == null) {
            container.add(selector, 0);
            selector.setVisible(true);
        }
        selector.setBounds(bounds);
    }

    /** ラバーバンドセレクタ非表示，削除． */
    public void drawSelected(Rectangle bounds) {
        selector.setVisible(false);
        container.remove(selector);
    }

    /** 移動時のラバーバンドを描画します． */
    public void drawMoving(Rectangle r) {
    }

    /** 移動後のラバーバンドを描画します． */
    public void drawMoved(Rectangle r) {
    }

    /** リサイズ時のラバーバンドを描画します． */
    public void drawResizing(Rectangle r) {
    }

    /** リサイズ後のラバーバンドを描画します． */
    public void drawResized(Rectangle r) {
    }

    /**
     * マウスの位置によってリサイズか移動かを取得します．
     */
    public int getMode(Component component, Point point) {
        int mode;

        int x = point.x;
        int y = point.y;

        Insets ri = resizingInsets;

        int minX = ri.left;
        int maxX = -ri.right + component.getSize().width;
        int minY = ri.top;
        int maxY = -ri.bottom + component.getSize().height;

        if (x < minX && y < minY) { // 左上にリサイズ
            mode = RubberBand.RESIZE_NW;
        } else if (x < minX && y > maxY) { // 左下にリサイズ
            mode = RubberBand.RESIZE_SW;
        } else if (x > maxX && y < minY) { // 右上にリサイズ
            mode = RubberBand.RESIZE_NE;
        } else if (x > maxX && y > maxY) { // 右下にリサイズ
            mode = RubberBand.RESIZE_SE;
        } else if ((x > minX && x < maxX) && y < minY) { // 上にリサイズ
            mode = RubberBand.RESIZE_N;
        } else if ((x > minX && x < maxX) && y > maxY) { // 下にリサイズ
            mode = RubberBand.RESIZE_S;
        } else if (x < minX && (y > minY && y < maxY)) { // 左にリサイズ
            mode = RubberBand.RESIZE_W;
        } else if (x > maxX && (y > minY && y < maxY)) { // 右にリサイズ
            mode = RubberBand.RESIZE_E;
        } else { // 移動モード
            mode = RubberBand.MOVE_MODE;
        }

        return mode;
    }

    /**
     * @see RubberBand#getMode()
     * @param mode
     */
    public Cursor getCursor(int mode) {
        return cursors[mode];
    }
}

/* */
