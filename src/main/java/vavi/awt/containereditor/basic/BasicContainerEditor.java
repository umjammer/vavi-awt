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
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.MouseInputListener;

import vavi.awt.Selectable;
import vavi.awt.containereditor.ContainerEditor;
import vavi.awt.containereditor.LocatableController;
import vavi.awt.rubberband.RubberBandAdapter;
import vavi.awt.rubberband.RubberBandEvent;
import vavi.awt.rubberband.RubberBandListener;
import vavi.swing.event.EditorEvent;
import vavi.swing.event.EditorListener;

import static java.lang.System.getLogger;


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

    private static final Logger logger = getLogger(BasicContainerEditor.class.getName());

    /** */
    private final GlassPane glassPane;

    /** */
    public BasicContainerEditor(Container container) throws HeadlessException {
        super(container);

        if (container.getParent() == null) {
            throw new HeadlessException("no parent for: " + container);
        }

        parent = container.getParent();

logger.log(Level.TRACE, container.getSize());
//      container.setLayout(null);
//      container.setSize(container.getPreferredSize());

        glassPane = new GlassPane(container);
        glassPane.addEditorListener(el);
        glassPane.addRubberBandListener(rbl);
    }

    @Override
    public void setContainer(Container container) {
        glassPane.disableContainer();

        super.setContainer(container);

        glassPane.setContainer(container);
        if (isEditable()) {
            glassPane.enableContainer();
        }
    }

    /** */
    private final Container parent;

    @Override
    public void setEditable(boolean isEditable) {
logger.log(Level.TRACE, isEditable + ": " + container.hashCode());
        super.setEditable(isEditable);
        if (isEditable) {
            parent.remove(container);
            glassPane.enableContainer();
            parent.add(glassPane);
            glassPane.setBounds(container.getBounds());
            glassPane.doLayout();
logger.log(Level.TRACE, container.getSize());
        } else {
            parent.remove(glassPane);
            glassPane.disableContainer();
            parent.add(container);
            container.setBounds(glassPane.getBounds());
        }
    }

    // ----

    /** editor listener for rubberband gesture */
    private final EditorListener el = new EditorListener() {
        @Override public void editorUpdated(EditorEvent ev) {
            String name = ev.getName();
            if ("clicked".equals(name)) {
                if (ev.getArguments() == null) { // Container
                    // Let's say we select container.
                    select(container, false);
                } else { // BasicController
                    Object[] args = ev.getArguments();
logger.log(Level.TRACE, args[0]);
                    Selectable selectable = (Selectable) args[0];
                    boolean isMultiSelect = (Boolean) args[1];
                    selectionModel.select(selectable, isMultiSelect);
                }
                glassPane.repaint();
            }
        }
    };

    /** A rubber band listener for a rubber band gesture */
    private final RubberBandListener rbl = new RubberBandAdapter() {
        @Override
        public void selected(RubberBandEvent ev) {
            List<LocatableController> selected = getControllersIn(ev.getBounds());
            selectionModel.select(selected.toArray(new Selectable[0]));
            glassPane.repaint();
        }

        @Override
        public void moving(RubberBandEvent ev) {
            List<Selectable> selected = selectionModel.getSelected();
            for (Selectable selectable : selected) {
                BasicController bc = (BasicController) selectable;
                bc.setLocation(getLocationFor(bc, ev.getLocation()));
            }
        }

        @Override
        public void moved(RubberBandEvent ev) {
            List<Selectable> selected = selectionModel.getSelected();
            for (Selectable selectable : selected) {
                BasicController bc = (BasicController) selectable;
                bc.setLocation(getLocationFor(bc, ev.getLocation()));

                Component c = bc.getView();
                fireEditorUpdated(new EditorEvent(BasicContainerEditor.this, "location", c));
            }
            glassPane.repaint();
        }

        @Override
        public void resizing(RubberBandEvent ev) {
            List<Selectable> selected = selectionModel.getSelected();
            for (Selectable selectable : selected) {
                BasicController bc = (BasicController) selectable;
                bc.setBounds(getBoundsFor(bc, ev.getBounds()));
            }
        }

        @Override
        public void resized(RubberBandEvent ev) {
            List<Selectable> selected = selectionModel.getSelected();
            for (Selectable selectable : selected) {
                BasicController bc = (BasicController) selectable;
                bc.setBounds(getBoundsFor(bc, ev.getBounds()));

                Component c = bc.getView();
                fireEditorUpdated(new EditorEvent(BasicContainerEditor.this, "bounds", c));
            }
            glassPane.repaint();
        }
    };

    /**
     * Returns controllers inside bounds.
     */
    private List<LocatableController> getControllersIn(Rectangle r) {

        List<LocatableController> selected = new ArrayList<>();

        for (int i = 0; i < container.getComponentCount(); i++) {
            Component c = container.getComponent(i);

            // TODO check RubberBandListener#selected(Rectangle) specs. change
            if (!(c instanceof LocatableController) && r.x < c.getLocation().x && r.y < c.getLocation().y && r.x + r.width > c.getLocation().x + c.getSize().width && r.y + r.height > c.getLocation().y + c.getSize().height) {

                LocatableController controller = glassPane.getControllerFor(c);
                selected.add(controller);
            }
        }

        return selected;
    }

    /** Apply difference to a component */
    private Point getLocationFor(Component component, Point dp) {
        Point tp = component.getLocation();
        tp.x += dp.x;
        tp.y += dp.y;
        return tp;
    }

    /** Apply difference to a component */
    private Rectangle getBoundsFor(Component component, Rectangle dr) {
        Rectangle tr = component.getBounds();
        tr.x += dr.x;
        tr.y += dr.y;
        tr.width += dr.width;
        tr.height += dr.height;
        return tr;
    }

    // ----

    /**
     * InputMap supports this in the future?
     */
    @Override
    public void setMouseInputAction(MouseInputListener mil) {
        glassPane.setMouseInputAction(mil);
    }

    /** TODO i don't like it */
    @Override
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
