/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.containereditor.basic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.event.MouseInputListener;

import vavi.awt.Selectable;
import vavi.awt.containereditor.ContainerEditor;
import vavi.awt.containereditor.LocatableController;
import vavi.awt.rubberband.RubberBandAdapter;
import vavi.awt.rubberband.RubberBandEvent;
import vavi.awt.rubberband.RubberBandListener;
import vavi.swing.event.EditorEvent;
import vavi.swing.event.EditorListener;
import vavi.util.Debug;


/**
 * BasicContainerEditor
 *
 * @event EditorEvent("location", Component)
 * @event EditorEvent("bounds", Component)
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020604 nsano initial version <br>
 *          0.01 020609 nsano delete cse -> editor event <br>
 *          0.10 020611 nsano fix <br>
 *          0.11 020613 nsano refine <br>
 *          0.12 020617 nsano add grid function <br>
 */
public class BasicContainerEditor extends ContainerEditor {

    //
    private final Object thiz = this;

    /** */
    private GlassPane glassPane;

    /** */
    public BasicContainerEditor(Container container) throws HeadlessException {
        super(container);

        if (container.getParent() == null) {
            throw new HeadlessException("no parent for: " + container);
        }

        parent = container.getParent();

Debug.println(Level.FINER, container.getSize());
//      container.setLayout(null);
//      container.setSize(container.getPreferredSize());

        glassPane = new GlassPane(container);
        glassPane.addEditorListener(el);
        glassPane.addRubberBandListener(rbl);
    }

    /** */
    public void setContainer(Container container) {
        glassPane.disableContainer();

        super.setContainer(container);

        glassPane.setContainer(container);
        if (isEditable()) {
            glassPane.enableContainer();
        }
    }

    /** */
    private Container parent;

    /** コンテナが編集可能かどうかを設定します． */
    public void setEditable(boolean isEditable) {
Debug.println(Level.FINER, isEditable + ": " + container.hashCode());
        super.setEditable(isEditable);
        if (isEditable) {
            parent.remove(container);
            glassPane.enableContainer();
            parent.add(glassPane);
            glassPane.setBounds(container.getBounds());
            glassPane.doLayout();
Debug.println(Level.FINER, container.getSize());
        } else {
            parent.remove(glassPane);
            glassPane.disableContainer();
            parent.add(container);
            container.setBounds(glassPane.getBounds());
        }
    }

    // ---------------------------------------------------------------------

    /** editor listener for rubberband gesture */
    private EditorListener el = new EditorListener() {
        public void editorUpdated(EditorEvent ev) {
            String name = ev.getName();
            if ("clicked".equals(name)) {
                if (ev.getArguments() == null) { // Container
                    // container を選択したことにする．
                    select(container, false);
                } else { // BasicController
                    Object[] args = ev.getArguments();
Debug.println(Level.FINER, args[0]);
                    Selectable selectable = (Selectable) args[0];
                    boolean isMultiSelect = (Boolean) args[1];
                    selectionModel.select(selectable, isMultiSelect);
                }
                glassPane.repaint();
            }
        }
    };

    /** rubberband listener for rubberband gesture */
    private RubberBandListener rbl = new RubberBandAdapter() {
        /** 選択 */
        public void selected(RubberBandEvent ev) {
            List<LocatableController> selected = getControllersIn(ev.getBounds());
            selectionModel.select(selected.toArray(new Selectable[0]));
            glassPane.repaint();
        }

        /** 移動中 */
        public void moving(RubberBandEvent ev) {
            List<Selectable> selected = selectionModel.getSelected();
            for (Selectable selectable : selected) {
                BasicController bc = (BasicController) selectable;
                bc.setLocation(getLocationFor(bc, ev.getLocation()));
            }
        }

        /** 移動終了 */
        public void moved(RubberBandEvent ev) {
            List<Selectable> selected = selectionModel.getSelected();
            for (Selectable selectable : selected) {
                BasicController bc = (BasicController) selectable;
                bc.setLocation(getLocationFor(bc, ev.getLocation()));

                Component c = bc.getView();
                fireEditorUpdated(new EditorEvent(thiz, "location", c));
            }
            glassPane.repaint();
        }

        /** リサイズ中 */
        public void resizing(RubberBandEvent ev) {
            List<Selectable> selected = selectionModel.getSelected();
            for (Selectable selectable : selected) {
                BasicController bc = (BasicController) selectable;
                bc.setBounds(getBoundsFor(bc, ev.getBounds()));
            }
        }

        /** リサイズ中 */
        public void resized(RubberBandEvent ev) {
            List<Selectable> selected = selectionModel.getSelected();
            for (Selectable selectable : selected) {
                BasicController bc = (BasicController) selectable;
                bc.setBounds(getBoundsFor(bc, ev.getBounds()));

                Component c = bc.getView();
                fireEditorUpdated(new EditorEvent(thiz, "bounds", c));
            }
            glassPane.repaint();
        }
    };

    /**
     * 領域内の Controller 返します．
     */
    private List<LocatableController> getControllersIn(Rectangle r) {

        List<LocatableController> selected = new ArrayList<>();

        for (int i = 0; i < container.getComponentCount(); i++) {
            Component c = container.getComponent(i);

            if (!(c instanceof LocatableController) && r.x < c.getLocation().x && r.y < c.getLocation().y && r.width > c.getLocation().x + c.getSize().width && r.height > c.getLocation().y + c.getSize().height) {

                LocatableController controller = glassPane.getControllerFor(c);
                selected.add(controller);
            }
        }

        return selected;
    }

    /** 差をコンポーネントに適用 */
    private Point getLocationFor(Component component, Point dp) {
        Point tp = component.getLocation();
        tp.x += dp.x;
        tp.y += dp.y;
        return tp;
    }

    /** 差をコンポーネントに適用 */
    private Rectangle getBoundsFor(Component component, Rectangle dr) {
        Rectangle tr = component.getBounds();
        tr.x += dr.x;
        tr.y += dr.y;
        tr.width += dr.width;
        tr.height += dr.height;
        return tr;
    }

    // -------------------------------------------------------------------------

    /**
     * 将来 InputMap が対応してくれんじゃないの？
     */
    public void setMouseInputAction(MouseInputListener mil) {
        glassPane.setMouseInputAction(mil);
    }

    /** TODO 気に入らん */
    protected LocatableController getControllerFor(Component component) {
        return glassPane.getControllerFor(component);
    }

    /** TODO */
    public void setGridSize(Dimension gridSize) {
        glassPane.setGridSize(gridSize);
        glassPane.repaint();
    }

    /** TODO */
    public void setGridEnabled(boolean gridEnabled) {
        glassPane.setGridEnabled(gridEnabled);
        glassPane.repaint();
    }

    /** TODO */
    public void setComponentBounds(Component component, Rectangle bounds) {
        LocatableController controller = getControllerFor(component);
        controller.setBounds(bounds);
    }
}

/* */
