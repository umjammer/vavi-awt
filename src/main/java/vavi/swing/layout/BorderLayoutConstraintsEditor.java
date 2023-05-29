/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * BorderLayoutConstraintEditor.
 *
 * @todo get constraint when initializing components in the container.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 */
public class BorderLayoutConstraintsEditor
    extends AbstractLayoutConstraintsEditor {

    /** Creates border constraints editor */
    public BorderLayoutConstraintsEditor(BorderLayout layout) {
        super(layout);
    }

    //-------------------------------------------------------------------------

    /** when constraints updated */
    private PropertyChangeListener pcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {
            Component component = container.getComponent(index);
Debug.println(Level.FINE, index);
            layout.removeLayoutComponent(component);
            layout.addLayoutComponent(component, ev.getNewValue());

            layout.layoutContainer(container);
        }
    };

    /** sets target constraint */
    public void setLayoutConstraints(int index,LayoutConstraints constraints) {
        super.setLayoutConstraints(index, constraints);
        constraints.addPropertyChangeListener(pcl);
    }

    // constraints supports ---------------------------------------------------

    /**
     * Associates constraints.
     * @param index component no. in container
     */
    public Object associateConstraints(int index) {

        Point posInCont = container.getComponent(index).getLocation();

        String primary = BorderLayout.CENTER;
        String alternateX = null;
        String alternateY = null;

        int w = container.getSize().width;
        int h = container.getSize().height;

        Insets contInsets = container.getInsets();
        int marginW = getMargin(w - contInsets.left - contInsets.right);
        int marginH = getMargin(h - contInsets.top - contInsets.bottom);

        int xC = 1; // center by default (0 - left, 1 - center, 2 - right)
        int yC = 1; // center by default (0 - top, 1 - center, 2 - bottom)

        if (w > 25) {
            if (posInCont.x < contInsets.left + marginW)
                xC = 0; // left
            else if (posInCont.x >= w - marginW - contInsets.right)
                xC = 2; // right
        }
        if (h > 25) {
            if (posInCont.y < contInsets.top + marginH)
                yC = 0; // top
            else if (posInCont.y >= h - marginH - contInsets.bottom)
                yC = 2; // bottom
        }

        if (xC == 0) {
            primary = BorderLayout.WEST;
        } else if (xC == 2) {
            primary = BorderLayout.EAST;
        } else {
            alternateX = posInCont.x - contInsets.left <
                (w - contInsets.left - contInsets.right) / 2 ?
                BorderLayout.WEST : BorderLayout.EAST;
        }

        if (yC == 0) { // top
            alternateX = primary;
            primary = BorderLayout.NORTH;
        } else if (yC == 2) { // bottom
            alternateX = primary;
            primary = BorderLayout.SOUTH;
        } else {
            alternateY = posInCont.y - contInsets.top <
                (h - contInsets.top - contInsets.bottom) / 2 ?
                BorderLayout.NORTH : BorderLayout.SOUTH;
        }

        String[] suggested = new String[] { primary, alternateY, alternateX };
        String[] free = findFreePositions();

        for (int i = 0; i < suggested.length; i++) {
            String str = suggested[i];
            if (str == null) {
                continue;
            }

            for (int j = 0; j < free.length; j++) {
                if (free[j].equals(str)) {
                    return str;
                }
            }
        }

        return free[0];
    }

    /** */
    private String[] findFreePositions() {
        List<String> positions = new ArrayList<>(6);

        if (getComponentOnPosition(BorderLayout.CENTER) == -1) {
            positions.add(BorderLayout.CENTER);
        }
        if (getComponentOnPosition(BorderLayout.NORTH) == -1) {
            positions.add(BorderLayout.NORTH);
        }
        if (getComponentOnPosition(BorderLayout.SOUTH) == -1) {
            positions.add(BorderLayout.SOUTH);
        }
        if (getComponentOnPosition(BorderLayout.EAST) == -1) {
            positions.add(BorderLayout.EAST);
        }
        if (getComponentOnPosition(BorderLayout.WEST) == -1) {
            positions.add(BorderLayout.WEST);
        }
        if (positions.size() == 0) {
            positions.add(BorderLayout.CENTER);
        }

        String[] free = new String[positions.size()];
        positions.toArray(free);
        return free;
    }

    /** */
    private int getComponentOnPosition(String position) {
        for (int i = 0; i < cache.size(); i++) {
            LayoutConstraints lc = cache.get(String.valueOf(i));
            Object constraints = lc.getConstraints();
            if (constraints != null && position.equals(constraints)) {
                return i;
            }
        }

        return -1;
    }

    /** */
    private int getMargin(int size) {
        int margin = size / 8;
        if (margin < 10) margin = 10;
        if (margin > 50) margin = 50;
        return margin;
    }
}

/* */
