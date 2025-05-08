/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.containereditor.basic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import vavi.util.Debug;

import static java.lang.System.getLogger;


/**
 * GlassPaneLayout
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020613 nsano initial version <br>
 */
public class GlassPaneLayout implements LayoutManager {

    private static final Logger logger = getLogger(GlassPaneLayout.class.getName());

    /**
     * this class doesn't use this method.
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Places the container in the specified panel.
     */
    @Override
    public void layoutContainer(Container parent) {
logger.log(Level.TRACE, Debug.getCallerMethod());
logger.log(Level.TRACE, parent.getPreferredSize().width + ", " + parent.getPreferredSize().height);
logger.log(Level.TRACE, parent);
logger.log(Level.TRACE, parent.getSize().width + ", " + parent.getSize().height);
        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component component = parent.getComponent(i);
            component.setLocation(new Point(0, 0));
            component.setSize(parent.getSize());
        }
    }

    /**
     * Calculates the minimum size the panel will have
     * when the components are placed in the specified parent container.
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
logger.log(Level.DEBUG, parent.getSize().width + ", " + parent.getSize().height);
        return parent.getSize();
    }

    /**
     * Calculates the preferred size of the panel
     * when the components are placed in the specified parent container.
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
logger.log(Level.DEBUG, parent.getSize().width + ", " + parent.getSize().height);
        return parent.getSize();
    }

    /**
     * this class doesn't use this method.
     */
    @Override
    public void removeLayoutComponent(Component comp) {
    }
}
