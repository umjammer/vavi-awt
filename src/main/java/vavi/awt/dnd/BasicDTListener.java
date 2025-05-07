/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import static java.lang.System.getLogger;


/**
 * The basic DTListener a listener that tracks the state of the operation.
 *
 * @see java.awt.dnd.DropTargetListener
 * @see java.awt.dnd.DropTarget
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 */
public abstract class BasicDTListener implements DropTargetListener {

    private static final Logger logger = getLogger(BasicDTListener.class.getName());

    /** The drag action */
    protected int dragAction = DnDConstants.ACTION_MOVE;

    /**
     * Called by isDragOk. Checks to see if the flavor drag flavor is
     * acceptable.
     *
     * @param ev the DropTargetDragEvent object
     * @return whether the flavor is acceptable
     */
    protected abstract boolean isDragFlavorSupported(DropTargetDragEvent ev);

    /**
     * Called by drop. Checks the flavors and operations.
     *
     * @param ev the DropTargetDropEvent object
     * @return the chosen DataFlavor or null if none match
     */
    protected abstract DataFlavor chooseDropFlavor(DropTargetDropEvent ev);

    /**
     * Called by dragEnter and dragOver. Checks the flavors and operations.
     *
     * @param ev the event object
     * @return whether the flavor and operation is ok
     */
    private boolean isDragOk(DropTargetDragEvent ev) {

        if (!isDragFlavorSupported(ev)) {
logger.log(Level.WARNING, "no flavors chosen");
            return false;
        }

        // the actions specified when the source
        // created the DragGestureRecognizer
        // int sa = ev.getSourceActions();

        // the docs on DropTargetDragEvent rejectDrag says that
        // the dropAction should be examined
        int da = ev.getDropAction();
logger.log(Level.TRACE, "drop action: " + da + " my acceptable actions " + dragAction);

        // we're saying that these actions are necessary
        return (da & dragAction) != 0;
    }

    /**
     * start "drag under" feedback on component invoke acceptDrag or rejectDrag
     * based on isDragOk
     * When overriding, don't forget to use <code>super.dragEnter(ev)</code>.
     * @callsuper
     */
    @Override
    public void dragEnter(DropTargetDragEvent ev) {
logger.log(Level.TRACE, ev);
        if (!isDragOk(ev)) {
logger.log(Level.WARNING, "not ok");
            ev.rejectDrag();
            return;
        }
logger.log(Level.TRACE, "accepting: " + ev.getDropAction());
        ev.acceptDrag(ev.getDropAction());
    }

    /**
     * continue "drag under" feedback on component invoke acceptDrag or
     * rejectDrag based on isDragOk
     * When overriding, don't forget to use <code>super.dragOver(ev)</code>.
     * @callsuper
     */
    @Override
    public void dragOver(DropTargetDragEvent ev) {

        if (!isDragOk(ev)) {
logger.log(Level.WARNING, "not ok");
            ev.rejectDrag();
            return;
        }
logger.log(Level.TRACE, "accepting");
        ev.acceptDrag(ev.getDropAction());
    }

    /**
     * When overriding, don't forget to include <code>super.dropActionChanged(ev)</code>.
     * @callsuper
     */
    @Override
    public void dropActionChanged(DropTargetDragEvent ev) {

        if (!isDragOk(ev)) {
            logger.log(Level.WARNING, "not ok");
            ev.rejectDrag();
            return;
        }
logger.log(Level.TRACE, "accepting: " + ev.getDropAction());
        ev.acceptDrag(ev.getDropAction());
    }

    /**
     * Called when the drag action ends.
     * When overriding, there is no need to call <code>super.dragExit(ev)</code>.
     */
    @Override
    public void dragExit(DropTargetEvent ev) {
logger.log(Level.TRACE, ev);
    }

    /**
     * perform action from getSourceActions on the transferable invoke
     * acceptDrop or rejectDrop invoke dropComplete if its a local (same JVM)
     * transfer, use StringTransferable.localStringFlavor find a match for the
     * flavor check the operation get the transferable according to the chosen
     * flavor do the transfer
     */
    @Override
    public void drop(DropTargetDropEvent ev) {
logger.log(Level.TRACE, ev);

        DataFlavor chosen = chooseDropFlavor(ev);
        if (chosen == null) {
logger.log(Level.WARNING, "No flavor match found");
            ev.rejectDrop();
            return;
        }
logger.log(Level.TRACE, "data flavor is " + chosen.getMimeType());

        // the actual operation
        // int da = ev.getDropAction();
        // the actions that the source has specified with
        // DragGestureRecognizer
        int sa = ev.getSourceActions();
logger.log(Level.TRACE, "sourceActions: " + sa);
logger.log(Level.TRACE, "dropAction: " + ev.getDropAction());

        if ((sa & dragAction) == 0) {
logger.log(Level.WARNING, "No action match found");
            ev.rejectDrop();
            return;
        }

        Object data = null;
        try {
            /*
             * the source listener receives this action in dragDropEnd. if the
             * action is DnDConstants.ACTION_COPY_OR_MOVE then the source
             * receives MOVE!
             */
            ev.acceptDrop(dragAction);

            data = ev.getTransferable().getTransferData(chosen);
        } catch (Throwable e) { // TODO
logger.log(Level.ERROR, e.getMessage(), e);
            ev.dropComplete(false);
            return;
        }

        ev.dropComplete(dropImpl(ev, data));
    }

    /**
     * You need to implement here dropping procedure.
     *
     * @param ev the event object
     * @param data Opportunistic dropped data
     * @return Was the drop successful?
     */
    protected abstract boolean dropImpl(DropTargetDropEvent ev, Object data);
}
