/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.containereditor.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import vavi.awt.containereditor.LocatableController;
import vavi.awt.rubberband.RubberBandGesture;
import vavi.awt.rubberband.RubberBandListener;
import vavi.swing.event.EditorListener;

import static java.lang.System.getLogger;


/**
 * GlassPane
 *
 * must setSize for container
 * TODO still remaining container editor elements, separate pure rubber band
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020613 nsano initial version <br>
 *          0.01 020617 nsano add grid function <br>
 */
public class GlassPane extends JComponent {

    private static final Logger logger = getLogger(GlassPane.class.getName());

    /** */
    private final JComponent glassPane = new JComponent() {
        private final ContainerListener cl = new ContainerListener() {
            @Override
            public void componentAdded(ContainerEvent ev) {
                Component component = ev.getChild();
                component.addMouseListener(rbg);
                component.addMouseMotionListener(rbg);
logger.log(Level.TRACE, "add gesture to: " + component.getName());
            }
            @Override
            public void componentRemoved(ContainerEvent ev) {
                Component component = ev.getChild();
                component.removeMouseListener(rbg);
                component.removeMouseMotionListener(rbg);
logger.log(Level.TRACE, "remove gesture to: " + component.getName());
            }
        };
        {
            setName("glassPane");
            setLayout(null);
            setDoubleBuffered(true);
            addContainerListener(cl);
        }
        @Override
        public void paintComponent(Graphics g) {
            if (gridEnabled) {
                g.setColor(Color.yellow);
                Dimension size = getSize();
                int w = gridSize.width;
                int h = gridSize.height;
                for (int y = h; y < size.height; y += h) {
                    g.drawLine(0, y, size.width - 1, y);
                }
                for (int x = w; x < size.width; x += w) {
                    g.drawLine(x, 0, x, size.height - 1);
                }
            }
        }
    };

    /** */
    private Container container;

    /** */
    private final RubberBandGesture rbg;

    /** */
    private Dimension gridSize;

    /** */
    private boolean gridEnabled;

    /** */
    public GlassPane(Container container) {
        this.container = container;

        this.setLayout(new GlassPaneLayout());
        this.setDoubleBuffered(true);

        rbg = new BasicRubberBandGesture(glassPane);

        this.add(glassPane, 0);
        glassPane.addMouseListener(rbg);
        glassPane.addMouseMotionListener(rbg);
    }

    /** */
    public void setGridSize(Dimension gridSize) {
        this.gridSize = gridSize;
    }

    /** */
    public void setGridEnabled(boolean gridEnabled) {
        this.gridEnabled = gridEnabled;
    }

    /** */
    public void enableContainer() {
        addAllControllers();
        container.addContainerListener(cl1);

        this.add(container, -1);
    }

    /** */
    public void disableContainer() {
        container.removeContainerListener(cl1);
        removeAllControllers();

        this.remove(container);
    }

    /** */
    public void setContainer(Container container) {
        this.container = container;
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
     * InputMap might resolve everything in the future
     */
    public void setMouseInputAction(MouseInputListener mil) {
        glassPane.addMouseListener(mil);
        glassPane.addMouseMotionListener(mil);
    }

    // ----

    /** Adding a component generates a controller for container */
    private final ContainerListener cl1 = new ContainerListener() {
        /** Adding a component generates a controller */
        @Override
        public void componentAdded(ContainerEvent ev) {
            Component component = ev.getChild();
            addController(component);
        }
        /** Deleting a component will delete the controller */
        @Override
        public void componentRemoved(ContainerEvent ev) {
            Component component = ev.getChild();
            removeController(component);
        }
    };

    // ----

    /** */
    private void addController(Component component) {
        Component controller = controllers.get(component);
        if (controller == null) {
            controller = new BasicController(component);
            glassPane.add(controller, 0);
logger.log(Level.TRACE, "add controller to: " + component.getName());
            controllers.put(component, controller);
        }
    }

    /** */
    private void removeController(Component component) {
        LocatableController controller = (LocatableController) controllers.get(component);
        if (controller != null && controller.getView() == component) {
            glassPane.remove((Component) controller);
logger.log(Level.TRACE, "remove controller for: " + component.getName());
            controllers.remove(component);
        }
    }

    // ----

    /** Create a Controller for every component */
    private void addAllControllers() {
logger.log(Level.TRACE, container.getComponentCount());
        for (int i = 0; i < container.getComponentCount(); i++) {
            Component component = container.getComponent(i);
            addController(component);
        }
    }

    /** Remove all Controllers */
    private void removeAllControllers() {
        for (int i = 0; i < container.getComponentCount(); i++) {
            Component component = container.getComponent(i);
            removeController(component);
        }
if (!controllers.isEmpty()) {
 logger.log(Level.INFO, "TODO " + controllers.size() + "controller(s) still alive");
}
    }

    // ----

    /** pairs for Component and Controller */
    private final Map<Component, Component> controllers = new HashMap<>();

    /** */
    public LocatableController getControllerFor(Component component) {
        return (LocatableController) controllers.get(component);
    }
}
