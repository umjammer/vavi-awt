/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.dnd;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import static java.lang.System.getLogger;


/**
 * Drag and drop class.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.10 010910 nsano fix #setDragAction <br>
 */
public abstract class Draggable {

    private static final Logger logger = getLogger(Draggable.class.getName());

    /** The drag source */
    private final DragSource dragSource;
    /** The drag gesture listener */
    private final DragGestureListener dgListener;
    /** the drag source listener */
    private final DragSourceListener dsListener;
    /** the drag gesture recognizer */
    private final DragGestureRecognizer dsRecognizer;
    /** The data */
    protected final Object data;
    /** The image for dragging */
    protected Image image;
    /** The component to drag */
    @SuppressWarnings("unused")
    private final Component source;
    /** The point for cursor */
    private static final Point point0 = new Point(0, 0);

    /**
     * Write an override process that returns the Transferable that will be dragged.
     * If no dragging is to occur, return null.
     * This method can be used as the initial step when starting a drag.
     * TODO Poor naming for the initial process of starting a drag
     * @see #dragDropEnd
     */
    protected abstract Transferable getTransferable(DragGestureEvent ev);

    /**
     * Write this by overriding the drag-and-drop termination process.
     */
    protected abstract void dragDropEnd(DragSourceEvent ev);

    /**
     * Sets the drag actions that will be accepted.
     */
    public void setDragAction(int dragAction) {
//      this.dragAction = dragAction;
        dsRecognizer.setSourceActions(dragAction);
    }

    /**
     * Sets the image to be displayed when dragging.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Initializes the dragging process.
     * When setting Draggable for each component, it is easier to specify the component's data in data.
     * For other cases (such as JTree), set data to null.
     *
     * @param source The component being dragged
     * @param data the real data
     */
    public Draggable(Component source, Object data) {

        this.source = source;

        this.data = data;

        dragSource = DragSource.getDefaultDragSource();
        dgListener = new DGListener();
        dsListener = new DSListener();

logger.log(Level.DEBUG, "image: " + DragSource.isDragImageSupported());
        // component, action, listener
        dsRecognizer = dragSource.createDefaultDragGestureRecognizer(
            source,
            DnDConstants.ACTION_MOVE,
            dgListener);
    }

    /** */
    private Cursor getCursor(int dragAction) {
        if ((dragAction & DnDConstants.ACTION_COPY) != 0) {
            return DragSource.DefaultCopyDrop;
//return Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        } else if ((dragAction & DnDConstants.ACTION_MOVE) != 0) {
//        return DragSource.DefaultMoveDrop;
return Toolkit.getDefaultToolkit().createCustomCursor(image, point0, "my cursor");
        } else if ((dragAction & DnDConstants.ACTION_LINK) != 0) {
            return DragSource.DefaultLinkDrop;
        } else {
            return DragSource.DefaultCopyNoDrop;
        }
    }

    /**
     * DragGestureListener
     * a listener that will start the drag.
     * has access to top level's dsListener and dragSource
     * @see java.awt.dnd.DragGestureListener
     * @see java.awt.dnd.DragSource
     * @see java.awt.datatransfer.StringSelection
     */
    private class DGListener implements DragGestureListener {

        /**
         * Start the drag if the operation is ok.
         * @param ev the event object
         */
        @Override
        public void dragGestureRecognized(DragGestureEvent ev) {

logger.log(Level.DEBUG, "-------------------------");
logger.log(Level.DEBUG, "accept action: " + dsRecognizer.getSourceActions() + ": " +
((dsRecognizer.getSourceActions()&DnDConstants.ACTION_COPY)!=0?"copy":"")+
((dsRecognizer.getSourceActions()&DnDConstants.ACTION_MOVE)!=0?", move":"")+
((dsRecognizer.getSourceActions()&DnDConstants.ACTION_LINK)!=0?", link":""));
logger.log(Level.DEBUG, "my action: " + ev.getDragAction() + ": " +
((ev.getDragAction() & DnDConstants.ACTION_COPY) != 0 ? "copy" : "") +
((ev.getDragAction() & DnDConstants.ACTION_MOVE) != 0 ? "move" : "") +
((ev.getDragAction() & DnDConstants.ACTION_LINK) != 0 ? "link" : ""));
            // if the action is ok we go ahead
            // otherwise we punt
            if ((ev.getDragAction() & dsRecognizer.getSourceActions()) == 0) {
                return;
            }

            Transferable transferable = getTransferable(ev);
            if (transferable == null) {
                return;
            }

            // now kick off the drag
            try {
                if (image != null) {
                    ev.startDrag(DragSource.DefaultCopyNoDrop,
                                 image,
                                 point0,
                                 transferable,
                                 dsListener);
                } else {
                    ev.startDrag(DragSource.DefaultCopyNoDrop,
                                 transferable,
                                 dsListener);
                }
            } catch (InvalidDnDOperationException e) {
logger.log(Level.DEBUG, e.getMessage(), e);
            }
        }
    }

    /**
     * DragSourceListener
     * a listener that will track the state of the DnD operation
     *
     * @see java.awt.dnd.DragSourceListener
     * @see java.awt.dnd.DragSource
     * @see java.awt.datatransfer.StringSelection
     */
    private class DSListener implements DragSourceListener {

        @Override
        public void dragDropEnd(DragSourceDropEvent ev) {
//            if (ev.getDropSuccess() == false) {
//                return;
//            }

            /*
             * the dropAction should be what the drop target specified
             * in acceptDrop
             */
logger.log(Level.TRACE, "action: " + ev.getDropAction());
            Draggable.this.dragDropEnd(ev);
        }

        @Override
        public void dragEnter(DragSourceDragEvent ev) {
logger.log(Level.TRACE, ev);
            DragSourceContext context = ev.getDragSourceContext();
            // intersection of the users selected action,
            // and the source and target actions
logger.log(Level.DEBUG, "my action: " + ev.getDropAction() + ": " +
((ev.getDropAction() & DnDConstants.ACTION_COPY) != 0 ? "copy" : "") +
((ev.getDropAction() & DnDConstants.ACTION_MOVE) != 0 ? "move" : "") +
((ev.getDropAction() & DnDConstants.ACTION_LINK) != 0 ? "link" : ""));
//            if ((myaction & Draggable.this.dragAction) != 0) {
//                context.setCursor(DragSource.DefaultCopyDrop);
                // Change the cursor.
                context.setCursor(getCursor(ev.getDropAction()));
//            } else {
//                context.setCursor(DragSource.DefaultCopyNoDrop);
//            }
        }

        @Override
        public void dragOver(DragSourceDragEvent ev) {
            DragSourceContext context = ev.getDragSourceContext();
            int sa = context.getSourceActions();
            int ua = ev.getUserAction();
            int da = ev.getDropAction();
            int ta = ev.getTargetActions();
logger.log(Level.TRACE, "dl dragOver source actions: " + sa);
logger.log(Level.TRACE, "user action: " + ua);
logger.log(Level.TRACE, "drop actions: " + da);
logger.log(Level.TRACE, "target actions: " + ta);
        }

        @Override
        public void dragExit(DragSourceEvent ev) {
logger.log(Level.TRACE, "exit: " + ev);
            DragSourceContext context = ev.getDragSourceContext();
        }

        @Override
        public void dropActionChanged(DragSourceDragEvent ev) {
            DragSourceContext context = ev.getDragSourceContext();
logger.log(Level.DEBUG, "my action: " + ev.getUserAction() + ": " +
((ev.getUserAction() & DnDConstants.ACTION_COPY) != 0 ? "copy" : "") +
((ev.getUserAction() & DnDConstants.ACTION_MOVE) != 0 ? "move" : "") +
((ev.getUserAction() & DnDConstants.ACTION_LINK) != 0 ? "link" : ""));
            // Change the cursor.
            // TODO Something like action = 0?
            context.setCursor(getCursor(ev.getUserAction()));
        }
    }
}
