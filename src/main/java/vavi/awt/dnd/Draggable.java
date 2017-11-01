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

import vavi.util.Debug;


/**
 * ドラッグアンドドロップができるクラス．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.10 010910 nsano fix #setDragAction <br>
 */
public abstract class Draggable {

    /** The drag source */
    private DragSource dragSource;
    /** The drag gesture listener */
    private DragGestureListener dgListener;
    /** the drag source listener */
    private DragSourceListener dsListener;
    /** the drag gesture recognizer */
    private DragGestureRecognizer dsRecognizer;
    /** The data */
    protected Object data;
    /** The image for dragging */
    protected Image image;
    /** The component to drag */
    @SuppressWarnings("unused")
    private Component source;
    /** The point for cursor */
    private static final Point point0 = new Point(0, 0);

    /**
     * ドラッグされる Transferable を返す処理をオーバライドして書いてください．
     * ドラッグしない場合は null を返すようにしてください．
     * このメソッドはドラッグ開始の初期処理として使用できます．
     * TODO ドラッグ開始の初期処理としてはネーミングが悪い
     * @see #dragDropEnd
     */
    protected abstract Transferable getTransferable(DragGestureEvent ev);

    /**
     * ドラッグアンドドロップの終了処理をオーバライドして書いてください．
     */
    protected abstract void dragDropEnd(DragSourceEvent ev);

    /**
     * 受け付けるドラッグアクションをセットします．
     */
    public void setDragAction(int dragAction) {
//      this.dragAction = dragAction;
        dsRecognizer.setSourceActions(dragAction);
    }

    /**
     * ドラッグ時のイメージをセットします．
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * ドラッグされる処理を初期化します．
     * 一つ一つのコンポーネントに Draggable を設定する際は
     * data にコンポーネントのデータを指定したほうが楽です．
     * それ以外 (JTree 等) は data は null にしておきます．
     * @param    source    ドラッグされるコンポーネント
     * @param    data    the real data
     */
    public Draggable(Component source, Object data) {

        this.source = source; 
    
        this.data = data;
    
        dragSource = DragSource.getDefaultDragSource();
        dgListener = new DGListener();
        dsListener = new DSListener();
    
Debug.println("image: " + DragSource.isDragImageSupported());
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
         * @param    ev    the event object
         */
        public void dragGestureRecognized(DragGestureEvent ev) {

Debug.println("-------------------------");
Debug.println("accept action: " + dsRecognizer.getSourceActions() + ": " +
((dsRecognizer.getSourceActions()&DnDConstants.ACTION_COPY)!=0?"copy":"")+
((dsRecognizer.getSourceActions()&DnDConstants.ACTION_MOVE)!=0?", move":"")+
((dsRecognizer.getSourceActions()&DnDConstants.ACTION_LINK)!=0?", link":""));
Debug.println("my action: " + ev.getDragAction() + ": " +
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
Debug.printStackTrace(e);
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

        /**
         * @param    ev    the event
         */
        public void dragDropEnd(DragSourceDropEvent ev) {
//            if (ev.getDropSuccess() == false) {
//                return;
//            }

            /*
             * the dropAction should be what the drop target specified
             * in acceptDrop
             */
//Debug.println(Debug.DEBUG, "action: " + ev.getDropAction());
            Draggable.this.dragDropEnd(ev);
        }

        /**
         * ドラッグ状態になったときに呼ばれます．
         * @param    ev    the event
         */
        public void dragEnter(DragSourceDragEvent ev) {
//Debug.println(Debug.DEBUG, ev);
            DragSourceContext context = ev.getDragSourceContext();
            // intersection of the users selected action,
            // and the source and target actions
Debug.println("my action: " + ev.getDropAction() + ": " +
((ev.getDropAction() & DnDConstants.ACTION_COPY) != 0 ? "copy" : "") +
((ev.getDropAction() & DnDConstants.ACTION_MOVE) != 0 ? "move" : "") +
((ev.getDropAction() & DnDConstants.ACTION_LINK) != 0 ? "link" : ""));
//            if ((myaction & Draggable.this.dragAction) != 0) {    
//                context.setCursor(DragSource.DefaultCopyDrop);
                // カーソルを変更します．
                context.setCursor(getCursor(ev.getDropAction()));
//            } else {
//                context.setCursor(DragSource.DefaultCopyNoDrop);
//            }
        }

        /**
         * @param    ev    the event
         */
        public void dragOver(DragSourceDragEvent ev) {
//            DragSourceContext context = ev.getDragSourceContext();
//            int sa = context.getSourceActions();
//            int ua = ev.getUserAction();
//            int da = ev.getDropAction();
//            int ta = ev.getTargetActions();
//Debug.println(Debug.DEBUG, "dl dragOver source actions: " + sa);
//Debug.println(Debug.DEBUG, "user action: " + ua);
//Debug.println(Debug.DEBUG, "drop actions: " + da);
//Debug.println(Debug.DEBUG, "target actions: " + ta);      
        }

        /**
         * @param    ev    the event
         */
        public void dragExit(DragSourceEvent ev) {
//Debug.println(Debug.DEBUG, "exit: " + ev);
//            DragSourceContext context = ev.getDragSourceContext();
        }

        /**
         * ドロップアクションが変更されたときに呼ばれます．
         * @param    ev    the event     
         */
        public void dropActionChanged(DragSourceDragEvent ev) {
            DragSourceContext context = ev.getDragSourceContext();
Debug.println("my action: " + ev.getUserAction() + ": " +
((ev.getUserAction() & DnDConstants.ACTION_COPY) != 0 ? "copy" : "") +
((ev.getUserAction() & DnDConstants.ACTION_MOVE) != 0 ? "move" : "") +
((ev.getUserAction() & DnDConstants.ACTION_LINK) != 0 ? "link" : ""));
            // カーソルを変更します．
            // TODO なんか action = 0 になるぞ？
            context.setCursor(getCursor(ev.getUserAction()));
        }
    }
}

/* */
