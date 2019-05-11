/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.containereditor.basic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;

import vavi.awt.Selectable;
import vavi.awt.rubberband.RubberBand;
import vavi.awt.rubberband.RubberBandAdapter;
import vavi.awt.rubberband.RubberBandEvent;
import vavi.awt.rubberband.RubberBandGesture;
import vavi.awt.rubberband.RubberBandListener;
import vavi.awt.rubberband.RubberBandRenderer;
import vavi.swing.event.EditorEvent;


/**
 * BasicRubberBandGesture
 *
 * @event EditorEvent("clicked", Object[] { Selectable, Boolean } | null)
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020603 nsano initial version <br>
 *          0.10 020611 nsano fix <br>
 *          0.11 020613 nsano refine <br>
 */
public class BasicRubberBandGesture extends RubberBandGesture {

    /** */
    private Container glassPane;

    /** ラバーバンドレンダラ */
    private RubberBandRenderer renderer;

    /** */
    public BasicRubberBandGesture(Container glassPane) {
        this.glassPane = glassPane;

        renderer = new BasicRubberBandRenderer(glassPane);

        rubberBand.addRubberBandListener(rbl);
    }

    //-------------------------------------------------------------------------

    /** rubberband listener for rubberband  */
    private RubberBandListener rbl = new RubberBandAdapter() {
        /** */
        public void selecting(RubberBandEvent ev) {
            renderer.drawSelecting(ev.getBounds());
        }
        /** */
        public void selected(RubberBandEvent ev) {
            renderer.drawSelected(ev.getBounds());
        }
        /** */
//        public void moving(RubberBandEvent ev) {
//            renderer.drawMoving(ev.getBounds());
//        }
        /** */
//        public void moved(RubberBandEvent ev) {
//            renderer.drawMoved(ev.getBounds());
//        }
        /** */
//        public void resizing(RubberBandEvent ev) {
//            renderer.drawResizing(ev.getBounds());
//        }
        /** */
//        public void resized(RubberBandEvent ev) {
//            renderer.drawResized(ev.getBounds());
//        }
    };

    //-------------------------------------------------------------------------

    /** */
    public void mouseClicked(MouseEvent ev) {
        Component component = ev.getComponent();
        Object args;
        if (component == glassPane) {
            args = null;
        } else { // Controller
            // Shift キーを押していたら複数選択にする
            Boolean isMultiSelect = Boolean.valueOf(ev.isShiftDown());
            args = new Object[] { component, isMultiSelect };

            // GlassController を最前に
            glassPane.remove(component);
            glassPane.add(component, 0);
        }

        fireEditorUpdated(new EditorEvent(this, "clicked", args));
    }

    /** */
    public void mousePressed(MouseEvent ev) {
        rubberBand.start(getLocationAtContainer(ev));
    }

    /** */
    public void mouseMoved(MouseEvent ev) {
        setMode(ev);
    }

    /** マウスがドラッグされたときに呼ばれます． */
    public void mouseDragged(MouseEvent ev) {
        rubberBand.doing(getLocationAtContainer(ev));
    }

    /** マウスがリリースされたときに呼ばれます． */
    public void mouseReleased(MouseEvent ev) {
        rubberBand.done(getLocationAtContainer(ev));
    }

    //-------------------------------------------------------------------------

    /**
     * マウスの位置によってリサイズか移動かを設定します．
     */
    private void setMode(MouseEvent ev) {
    Component component = ev.getComponent();

    if (component instanceof Selectable &&
            !((Selectable) component).isSelected()) {
        rubberBand.setMode(RubberBand.NORMAL_MODE);
    } else if (ev.getSource() == glassPane) {
        rubberBand.setMode(RubberBand.NORMAL_MODE);
    } else if (ev.getSource() == component) {
        rubberBand.setMode(renderer.getMode(component,ev.getPoint()));
    } else {
        rubberBand.setMode(RubberBand.MOVE_MODE);
    }

        component.setCursor(renderer.getCursor(rubberBand.getMode()));
    }

    /**
     * コンテナ上のポイントを返します．
     */
    private Point getLocationAtContainer(MouseEvent ev) {
        Component component = ev.getComponent();
        Point point = ev.getPoint();

        while (component != glassPane) {
            point.x += component.getLocation().x;
            point.y += component.getLocation().y;
            component = component.getParent();
        }

        return point;
    }
}

/* */
