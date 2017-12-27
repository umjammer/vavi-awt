/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.rubberband;

import javax.swing.event.MouseInputAdapter;

import vavi.swing.event.EditorEvent;
import vavi.swing.event.EditorListener;
import vavi.swing.event.EditorSupport;


/**
 * RubberBand (Model) を操作するコントローラです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020603 nsano initial version <br>
 */
public class RubberBandGesture extends MouseInputAdapter {

    /** */
    protected RubberBand rubberBand;

    /**
     * !!! You need to set renderer in your constructor.
     */
    public RubberBandGesture() {
        rubberBand = new RubberBand();
    }

    //-------------------------------------------------------------------------

    /** */
    public void addRubberBandListener(RubberBandListener rbl) {
        rubberBand.addRubberBandListener(rbl);
    }

    /** */
    public void removeRubberBandListener(RubberBandListener rbl) {
        rubberBand.removeRubberBandListener(rbl);
    }

    //-------------------------------------------------------------------------

    /** The editor support */
    private EditorSupport editorSupport = new EditorSupport();

    /** Adds an editor listener. */
    public void addEditorListener(EditorListener l) {
        editorSupport.addEditorListener(l);
    }

    /** Removes an editor listener. */
    public void removeEditorListener(EditorListener l) {
        editorSupport.removeEditorListener(l);
    }

    /** */
    protected void fireEditorUpdated(EditorEvent ev) {
        editorSupport.fireEditorUpdated(ev);
    }
}

/* */
