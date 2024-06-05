/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import vavi.awt.containereditor.basic.BasicRubberBandGesture;
import vavi.swing.event.EditorListener;


/**
 * GlassPane.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2018/02/17 umjammer initial version <br>
 */
public class GlassPane extends JComponent {

    /** */
    private RubberBandGesture rbg;

    /** */
    public GlassPane() {
        rbg = new BasicRubberBandGesture(this);

        setBackground(Color.red); // TODO what is this?
        addMouseListener(rbg);
        addMouseMotionListener(rbg);
    }

    /** */
    public void addEditorListener(EditorListener el) {
        rbg.addEditorListener(el);
    }

    /** */
    public void addRubberBandListener(RubberBandListener rbl) {
        rbg.addRubberBandListener(rbl);
    }

    /**
     * 将来 InputMap が対応してくれんじゃないの？
     */
    public void setMouseInputAction(MouseInputListener mil) {
        addMouseListener(mil);
        addMouseMotionListener(mil);
    }
}
