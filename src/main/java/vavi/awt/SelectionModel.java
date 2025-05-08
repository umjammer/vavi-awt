/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import vavi.awt.event.SelectionEvent;
import vavi.awt.event.SelectionListener;
import vavi.awt.event.SelectionSupport;

import static java.lang.System.getLogger;


/**
 * SelectionModel.
 *
 * @event SelectionEvent(Container, List<Selectable>)
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020529 nsano initial version <br>
 */
public class SelectionModel {

    private static final Logger logger = getLogger(SelectionModel.class.getName());

    /** A list of selected objects */
    private List<Selectable> selected;

    /** */
    public SelectionModel() {
        selected = new Vector<>();
    }

    /**
     * Returns a vector of selected Selectables.
     */
    public List<Selectable> getSelected() {
        return selected;
    }

    /**
     * Selects the specified vector of Selectables.
     */
    public void setSelected(List<Selectable> selected) {
logger.log(Level.INFO, "Warning: be careful to use this method.");
        this.selected = selected;

        fireValueChanged(new SelectionEvent(this, selected));
    }

    /**
     * Deselects all Selectables.
     */
    public void deselectAll() {
        for (Selectable selectable : selected) {
            selectable.setSelected(false);
        }

        selected.clear();

        fireValueChanged(new SelectionEvent(this, selected));
    }

    /**
     * Sets the Selectable to selected state.
     *
     * @param selectables
     */
    public void select(Selectable[] selectables) {

        deselectAll();

        for (Selectable selectable : selectables) {
            select(selectable, true);
        }
    }

    /**
     * Sets the Selectable to selected state.
     *
     * @param selectable Target Object
     * @param isMultiSelection Multiple choice or not
     */
    public void select(Selectable selectable, boolean isMultiSelection) {

logger.log(Level.TRACE, selectable);
        boolean isOldSelection = false;

        Iterator<Selectable> i = selected.iterator();
        while (i.hasNext()) {
            Selectable s = i.next();
            if (selectable == s) {
                if (isMultiSelection) {
                    // If the new selection is already selected, it will clear the selection.
                    s.setSelected(false);
                    i.remove();
                    isOldSelection = true;
                }
            }
        }

        if (!isOldSelection) {
            // When a new one is selected
            if (!isMultiSelection) {
                // Deselect all
                for (Selectable value : selected) {
                    value.setSelected(false);
                }
                selected.clear();
            }
            selected.add(selectable);

            // Select all selected
            for (Selectable value : selected) {
                value.setSelected(true);
            }
        }

        fireValueChanged(new SelectionEvent(this, selected));
    }

    // ----

    /** SelectionEvent mechanism utilities */
    private final SelectionSupport ss = new SelectionSupport();

    /** Attach a Selection listener. */
    public void addSelectionListener(SelectionListener l) {
        ss.addSelectionListener(l);
    }

    /** Removes the Selection listener. */
    public void removeSelectionListener(SelectionListener l) {
        ss.removeSelectionListener(l);
    }

    /** */
    protected void fireValueChanged(SelectionEvent ev) {
        ss.fireValueChanged(ev);
    }
}
