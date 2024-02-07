/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.containereditor;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.event.MouseInputListener;

import vavi.awt.Controller;
import vavi.awt.Selectable;
import vavi.awt.SelectionModel;
import vavi.awt.SelectionTransferable;
import vavi.awt.event.SelectionEvent;
import vavi.awt.event.SelectionListener;
import vavi.swing.Editable;
import vavi.swing.event.EditorEvent;
import vavi.swing.event.EditorListener;
import vavi.swing.event.EditorSupport;
import vavi.util.Debug;


/**
 * The Container editor using a SelectionModel.
 *
 * @event EditorEvent("copy", Vector<Component>)
 * @event EditorEvent("lostOwnership")
 * @event EditorEvent("select", Vector<Component>)
 *
 * TODO Is ComponentSelectionModel needed or not?
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020604 nsano initial version <br>
 *          0.10 020609 nsnao be editable <br>
 *          0.11 020609 nsnao implement clipboard <br>
 *          0.12 020609 nsnao remove css <br>
 *          0.13 020609 nsnao rubberband independent <br>
 *          0.14 020609 nsnao fix outer editor interface <br>
 *          0.15 020611 nsano change package <br>
 */
public abstract class ContainerEditor implements Editable {

    /** the selection model for components in the container */
    protected SelectionModel selectionModel;

    /** the container to edit. */
    protected Container container;

    /**
     * !!! you need to set new RubberBand in your constructor
     */
    public ContainerEditor(Container container) {
        this.container = container;

        selectionModel = new SelectionModel();
        selectionModel.addSelectionListener(sl);
    }

    /** */
    public void setContainer(Container container) {
        selectionModel.deselectAll();
        this.container = container;
    }

    // ----

    /**
     * 将来 InputMap が対応してくれんじゃないの？
     */
    public abstract void setMouseInputAction(MouseInputListener mil);

    // ----

    /** listener for selection model */
    private SelectionListener sl = new SelectionListener() {
        final List<Component> cs = new ArrayList<>();
        public void valueChanged(SelectionEvent ev) {
            cs.clear();
            @SuppressWarnings("unchecked")
            List<Selectable> selected = (List<Selectable>) ev.getSelected();
            for (Selectable selectable : selected) {
                cs.add(((Controller) selectable).getView());
            }
            fireEditorUpdated(new EditorEvent(ContainerEditor.this, "select", cs));
        }
    };

    // ----

    /** is the container editable? */
    private boolean isEditable = true;

    /** Returns the container is editable? */
    public boolean isEditable() {
        return isEditable;
    }

    /** Sets the container is editable? */
    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;

        selectionModel.deselectAll();
    }

    // for outer editor ----

    /** */
    protected abstract Controller getControllerFor(Component component);

    /**
     * 選択されている Component のリストを返します．
     */
    public List<Selectable> getSelected() {
        List<Selectable> selected = selectionModel.getSelected();
        List<Selectable> selection = new ArrayList<>();
        for (Selectable selectable : selected) {
            Controller controller = (Controller) selectable;
            selection.add((Selectable) controller.getView());
        }
        return selection;
    }

    /**
     * Make specified components selected.
     *
     * @param selection List<Selectable>
     */
    public void select(List<Selectable> selection) {
        List<Selectable> selected = new ArrayList<>();
        if (selection.size() == 1 && selection.get(0) == container) {
            selectionModel.deselectAll();
            selected.add((Selectable) container);
            fireEditorUpdated(new EditorEvent(ContainerEditor.this, "select", selected));
        } else {
            for (Selectable selectable : selection) {
                Component component = (Component) selectable;
                Controller controller = getControllerFor(component);
                selected.add(controller);
            }
            selectionModel.select(selected.toArray(new Selectable[0]));
        }
    }

    /**
     * Make a specified component selected.
     */
    public void select(Component component, boolean isMultiSelection) {
        if (component == container) {
            selectionModel.deselectAll();
            List<Component> selected = new ArrayList<>();
            selected.add(container);
            fireEditorUpdated(new EditorEvent(ContainerEditor.this, "select", selected));
        } else {
            Controller controller = getControllerFor(component);
            selectionModel.select(controller, isMultiSelection);
        }
    }

    private final List<Selectable> selected = new ArrayList<>();

    /**
     * Select all components.
     */
    public void selectAll() {
        selected.clear();
        for (int i = 0; i < container.getComponentCount(); i++) {
            Component component = container.getComponent(i);
            if (component instanceof Selectable) {
                selected.add((Selectable) component);
            }
        }
        selectionModel.select(selected.toArray(new Selectable[0]));
    }

    /**
     * Deselect all components.
     */
    public void deselectAll() {
        selectionModel.deselectAll();

        container.repaint();
    }

    // -------------------------------------------------------------------------

    /** */
    private final Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    /** */
    private final Clipboard localClipboard = new Clipboard(this.getClass().getName());

    /** */
    private Clipboard currentClipboard;

    /** */
    private ClipboardOwner clipboardOwner = new ClipboardOwner() {
        /** Called when lost the ownership. */
        public void lostOwnership(Clipboard clipboard, Transferable contents) {
            if (clipboard == systemClipboard) {
Debug.println(Level.FINE, clipboard.getName());
                localClipboard.setContents(contents, this);
                currentClipboard = localClipboard;
            } else {
Debug.println(Level.INFO, "???: " + clipboard.getName());
                fireEditorUpdated(new EditorEvent(ContainerEditor.this, "lostOwnership"));
            }
        }
    };

    /** Number of pasting the same component. */
    private int pasteCount;

    /**
     * Processes "cut".
     */
    public void cut() {
        copy();
        delete();
    }

    /**
     * Processes "copy".
     */
    public synchronized void copy() {

        try {
            pasteCount = 5;

            currentClipboard = systemClipboard;

            List<Selectable> selected = selectionModel.getSelected();
            Transferable transferable = new SelectionTransferable(selected);
            currentClipboard.setContents(transferable, clipboardOwner);

            fireEditorUpdated(new EditorEvent(this, "copy", selected));
        } catch (IOException e) {
Debug.println(Level.SEVERE, e);
        }
    }

    /**
     * Processes "past".
     */
    public synchronized void paste() {

        try {
            selectionModel.deselectAll();

            final DataFlavor flavor = SelectionTransferable.selectionFlavor;
            Transferable transferable = currentClipboard.getContents(this);
            @SuppressWarnings("unchecked")
            List<Selectable> selection = (List<Selectable>) transferable.getTransferData(flavor);
            for (Selectable selectable : selection) {
                LocatableController controller = (LocatableController) selectable;
                Point point = controller.getLocation();
                point.x += pasteCount;
                point.y += pasteCount;
                controller.setLocation(point);
                container.add(controller.getView());
            }

            pasteCount += 5;

            container.repaint();
        } catch (Exception e) {
Debug.println(Level.SEVERE, e);
Debug.printStackTrace(e);
        }
    }

    /**
     * Processes "delete".
     */
    public void delete() {
        List<Selectable> selected = selectionModel.getSelected();
        for (Selectable selectable : selected) {
            LocatableController controller = (LocatableController) selectable;
            container.remove(controller.getView());
        }

        selectionModel.deselectAll();

        container.repaint();
    }

    /**
     */
    public void deleteAll() {
        for (int i = 0; i < container.getComponentCount(); i++) {
            Component component = container.getComponent(i);
            if (!(component instanceof LocatableController)) {
                container.remove(component);
            }
        }

        selectionModel.deselectAll();

        container.repaint();
    }

    // ----

    /**
     * Processes "align upper".
     */
    public void alignTop() {

        List<Selectable> selected = selectionModel.getSelected();
        LocatableController c = (LocatableController) selected.get(0);
        int y = c.getLocation().y;

        for (Selectable selectable : selected) {
            c = (LocatableController) selectable;
            Point p = c.getLocation();
            p.y = y;
            c.setLocation(p);
        }

        container.validate();
        container.repaint();
    }

    /**
     * Processes "align left".
     */
    public void alignLeft() {

        List<Selectable> selected = selectionModel.getSelected();
        LocatableController c = (LocatableController) selected.get(0);
        int x = c.getLocation().x;

        for (Selectable selectable : selected) {
            c = (LocatableController) selectable;
            Point p = c.getLocation();
            p.x = x;
            c.setLocation(p);
        }

        container.validate();
        container.repaint();
    }

    /**
     * "右にそろえる"メニューの処理を行います．
     */
    public void alignRight() {

        List<Selectable> selected = selectionModel.getSelected();
        LocatableController c = (LocatableController) selected.get(0);
        int x = c.getLocation().x + c.getSize().width;

        for (Selectable selectable : selected) {
            c = (LocatableController) selectable;
            Point p = c.getLocation();
            p.x = x - c.getSize().width;
            c.setLocation(p);
        }

        container.validate();
        container.repaint();
    }

    /**
     * "下にそろえる"メニューの処理を行います．
     */
    public void alignBottom() {

        List<Selectable> selected = selectionModel.getSelected();
        LocatableController c = (LocatableController) selected.get(0);
        int y = c.getLocation().y + c.getSize().height;

        for (Selectable selectable : selected) {
            c = (LocatableController) selectable;
            Point p = c.getLocation();
            p.y = y - c.getSize().height;
            c.setLocation(p);
        }

        container.validate();
        container.repaint();
    }

    /**
     * "幅をそろえる"メニューの処理を行います．
     */
    public void alignWidth() {

        List<Selectable> selected = selectionModel.getSelected();
        LocatableController c = (LocatableController) selected.get(0);
        int w = c.getSize().width;

        for (Selectable selectable : selected) {
            c = (LocatableController) selectable;
            Dimension d = c.getSize();
            d.width = w;
            c.setSize(d);
        }

        container.validate();
        container.repaint();
    }

    /**
     * "高さをそろえる"メニューの処理を行います．
     */
    public void alignHeight() {

        List<Selectable> selected = selectionModel.getSelected();
        LocatableController c = (LocatableController) selected.get(0);
        int h = c.getSize().height;

        for (Selectable selectable : selected) {
            c = (LocatableController) selectable;
            Dimension d = c.getSize();
            d.height = h;
            c.setSize(d);
        }

        container.validate();
        container.repaint();
    }

    /**
     * "水平間隔を均等にする"メニューの処理を行います．
     */
    public void alignJustifyHorizontalGap() {

        List<Selectable> selected = selectionModel.getSelected();
        // 一つ目の部品
        LocatableController c1 = (LocatableController) selected.get(0);
        Point p1 = c1.getLocation();
        Dimension d1 = c1.getSize();
        // 二つ目の部品
        LocatableController c2 = (LocatableController) selected.get(1);
        Point p2 = c2.getLocation();
        // Dimension d2 = c2.getSize();

        int width = p2.x - (p1.x + d1.width);

        for (int i = 0; i < selected.size() - 1; i++) {
            // 一つ目の部品
            c1 = (LocatableController) selected.get(i);
            p1 = c1.getLocation();
            d1 = c1.getSize();

            // 二つ目の部品
            c2 = (LocatableController) selected.get(i + 1);
            p2 = c2.getLocation();
            // d2 = c2.getSize();

            // 差を計算
            int gap = width - (p2.x - (p1.x + d1.width));

            p2.x = p2.x + gap;
            c2.setLocation(p2);
        }

        container.validate();
        container.repaint();
    }

    /**
     * "垂直間隔を均等にする"メニューの処理を行います．
     */
    public void alignJustifyVerticalGap() {

        List<Selectable> selected = selectionModel.getSelected();
        // 一つ目の部品
        LocatableController c1 = (LocatableController) selected.get(0);
        Point p1 = c1.getLocation();
        Dimension d1 = c1.getSize();
        // 二つ目の部品
        LocatableController c2 = (LocatableController) selected.get(1);
        Point p2 = c2.getLocation();
//      Dimension d2 = c2.getSize();

        int height = p2.y - (p1.y + d1.height);

        for (int i = 0; i < selected.size() - 1; i++) {
            // 一つ目の部品
            c1 = (LocatableController) selected.get(i);
            p1 = c1.getLocation();
            d1 = c1.getSize();

            // 二つ目の部品
            c2 = (LocatableController) selected.get(i + 1);
            p2 = c2.getLocation();
            // d2 = c2.getSize();

            // 差を計算
            int gap = height - (p2.y - (p1.y + d1.height));

            p2.y = p2.y + gap;
            c2.setLocation(p2);
        }

        container.validate();
        container.repaint();
    }

    /**
     * "最前面に配置"メニューの処理を行います．
     */
    public void toFront() {

        List<Selectable> selected = selectionModel.getSelected();
        for (Selectable selectable : selected) {
            LocatableController controller = (LocatableController) selectable;
            Component component = controller.getView();
//          container.disableEvents(AWTEvent.CONTAINER_EVENT_MASK);
            container.remove(component);
            container.add(component, 0);
//          container.remove((Component) controller);
//          container.add((Component) controller, 0);
//          container.enableEvents(AWTEvent.CONTAINER_EVENT_MASK);
        }

        container.validate();
        container.repaint();
    }

    /**
     * "最背面に配置"メニューの処理を行います．
     */
    public void toBack() {

        List<Selectable> selected = selectionModel.getSelected();
        for (Selectable selectable : selected) {
            LocatableController controller = (LocatableController) selectable;
            Component component = controller.getView();
//          container.disableEvents(AWTEvent.CONTAINER_EVENT_MASK);
            container.remove(component);
            container.add(component, -1);
//          container.enableEvents(AWTEvent.CONTAINER_EVENT_MASK);
        }

        container.validate();
        container.repaint();
    }

    // -------------------------------------------------------------------------

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

    /** Fires an EditorEvent. */
    protected void fireEditorUpdated(EditorEvent ev) {
        editorSupport.fireEditorUpdated(ev);
    }
}

/* */
