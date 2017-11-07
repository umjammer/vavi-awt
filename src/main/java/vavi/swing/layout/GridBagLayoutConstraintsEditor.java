/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import vavi.util.Debug;


/**
 * GridLayoutConstraintEditor.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 */
public class GridBagLayoutConstraintsEditor
    extends AbstractLayoutConstraintsEditor {

    /** Creates layout constraints editor */
    public GridBagLayoutConstraintsEditor(GridBagLayout layout) {
        super(layout);
    }

    //-------------------------------------------------------------------------

    /** */
    private PropertyChangeListener pcl;

    /** TODO */
    public void setPropertyChangeListner(PropertyChangeListener pcl) {
        this.pcl = pcl;
    }

    /** sets target constraint */
    public void setLayoutConstraints(int index,LayoutConstraints constraints) {
        super.setLayoutConstraints(index, constraints);
        constraints.addPropertyChangeListener(pcl);
    }

    // constraints supports ---------------------------------------------------

    private static final int X_AXIS = 1;
    private static final int Y_AXIS = 2;

    /**
     * Associates constraints'.
     */
    public void associateConstraints() {

        int MAX_VALUE = 99999;
        int MIN_VALUE = -99999;

        Component[] components = container.getComponents();
        if (components.length == 0) {
Debug.println(components.length);
            return;
        }

        int[] axisX = new int[components.length + 1];
        int[] axisY = new int[components.length + 1];
        int[] crossingsX = new int[components.length + 1];
        int[] crossingsY = new int[components.length + 1];
        int axisXnumber = 1;
        int axisYnumber = 1;

        for (int i = 0; i < axisX.length; i++) {
            axisX[i] = MAX_VALUE;
            axisY[i] = MAX_VALUE;
        }

        // define the most left and right components.
        int minX = MAX_VALUE;
        int maxX = MIN_VALUE;
        int minY = MAX_VALUE;
        int maxY = MIN_VALUE;

        int mostLeft = 0;
//      int mostRight = 0;
        int mostTop = 0;
//      int mostBottom = 0;

        for (int i = 0; i < components.length; i++) {
            int x = components[i].getBounds().x;
            int x1 = x + components[i].getBounds().width;
            int y = components[i].getBounds().y;
            int y1 = y + components[i].getBounds().height;
            if (x < minX) {
                mostLeft = i;
                minX = x;
            }
            if (x1 > maxX) {
//              mostRight = i;
                maxX = x1;
            }
            if (y < minY) {
                mostTop = i;
                minY = y;
            }
            if (y1 > maxY) {
//              mostBottom = i;
                maxY = y1;
            }
        }
        // define basic axises, all right axises, but not if it's most right one...
        if (components.length > 1) {
            axisX[0] = MIN_VALUE;
            axisY[0] = MIN_VALUE;
            for (int i = 0; i < components.length; i++) {
                int x1 = components[i].getBounds().x + components[i].getBounds().width;
                if (x1 != maxX) {
                    axisX[axisXnumber] = x1;
                    axisXnumber++;
                }
                int y1 = components[i].getBounds().y + components[i].getBounds().height;
                if (y1!= maxY) {
                    axisY[axisYnumber] = y1;
                    axisYnumber++;
                }
            }
            Arrays.sort(axisX);
            Arrays.sort(axisY);

            // define basic crossings (i.e. number of components which are
            // crossed by an axis); the algorithm is trying to minimize the
            // crossings
            for (int i = 1; i < axisXnumber; i++) {
                crossingsX[i] = getCrossings(components, X_AXIS, axisX[i]);
            }
            for (int i = 1; i < axisYnumber; i++) {
                crossingsY[i] = getCrossings(components, Y_AXIS, axisY[i]);
            }

            // shift basic axis if the number of crossings for new place is
            // lower
            for (int i = 1; i < axisXnumber; i++) {
              for (int j = 0; j < components.length; j++) {
                if (j != mostLeft) {
                  int x = components[j].getBounds().x;
                  int x1 = x + components[j].getBounds().width;
                  if (x < axisX[i] && x > axisX[i-1] &&
                      crossingsX[i] > getCrossings(components, X_AXIS, x) &&
                      x != minX) {
                    axisX[i] = x;
                    crossingsX[i] = getCrossings(components, X_AXIS, x);
                  }
                  if (x1 > axisX[i] && x1 < axisX[i+1] &&
                      crossingsX[i] > getCrossings(components, X_AXIS, x1)) {
                    axisX[i] = x1;
                    crossingsX[i] = getCrossings(components, X_AXIS, x1);
                  }
                }
              }
            }

            for (int i = 1; i < axisYnumber; i++) {
              for (int j = 0; j < components.length; j++) {
                if (j != mostTop) {
                  int y = components[j].getBounds().y;
                  int y1 = y + components[j].getBounds().height;
                  if (y < axisY[i] && y > axisY[i-1] &&
                      crossingsY[i] > getCrossings(components, Y_AXIS, y) &&
                      y != minY) {
                    axisY[i] = y;
                    crossingsY[i] = getCrossings(components, Y_AXIS, y);
                  }
                  if (y1 > axisY[i] && y1 < axisY[i+1] &&
                      crossingsY[i] > getCrossings(components, Y_AXIS, y1)) {
                    axisY[i] = y1;
                    crossingsY[i] = getCrossings(components, Y_AXIS, y1);
                  }
                }
              }
            }

            // checking validity of all axis
            // checking if any axis is doubled (2 same axis)
            int removedX = 0;
            for (int i=1; i < axisXnumber; i++) {
                if (axisX[i] == axisX[i+1]) {
                    axisX[i] = MAX_VALUE;
                    removedX++;
                }
            }
            if (removedX > 0) {
                Arrays.sort(axisX);
                axisXnumber = axisXnumber - removedX;
            }
            int removedY = 0;
            for (int i = 1; i < axisYnumber; i++) {
                if (axisY[i] == axisY[i+1]) {
                    axisY[i] = MAX_VALUE;
                    removedY++;
                }
            }
            if (removedY > 0) {
                Arrays.sort(axisY);
                axisYnumber = axisYnumber - removedY;
            }
            // checking if any axis is redundand (i.e. no component is
            // fixing size of this axis)
            int last = axisX[0];
            removedX = 0;
            for (int i=1; i < axisXnumber; i++) {
                boolean removing = true;
                for (int j=0; j < components.length; j++) {
                    int x = components[j].getBounds().x;
                    int x1 = x + components[j].getBounds().width;
                    if (x < axisX[i] && x >= last && x1 <= axisX[i]) {
                        removing = false;
                        break;
                    }
                }
                last = axisX[i];
                if (removing) {
                    axisX[i] = MAX_VALUE;
                    removedX++;
                }
            }
            if (removedX > 0) {
                Arrays.sort(axisX);
                axisXnumber = axisXnumber - removedX;
            }
            last = axisY[0];
            removedY = 0;
            for (int i = 1; i < axisYnumber; i++) {
                boolean removing = true;
                for (int j=0; j < components.length; j++) {
                    int y = components[j].getBounds().y;
                    int y1 = y + components[j].getBounds().height;
                    if (y < axisY[i] && y >= last && y1 <= axisY[i]) {
                        removing = false;
                        break;
                    }
                }
                last = axisY[i];
                if (removing) {
                    axisY[i] = MAX_VALUE;
                    removedY++;
                }
            }
            if (removedY > 0) {
                Arrays.sort(axisY);
                axisYnumber = axisYnumber - removedY;
            }
            // removing most right and bottom axises if they are invalid
            if (axisX[axisXnumber - 1] == maxX)
                axisXnumber--;
            if (axisY[axisYnumber - 1] == maxY)
                axisYnumber--;
        }

        // seting first and last axis to proper values (i.e to form size)
        axisX[0] = 0;
        axisX[axisXnumber] = components[0].getParent().getSize().width;
        axisY[0] = 0;
        axisY[axisYnumber] = components[0].getParent().getSize().height;

        // define constraints based on axis
        for (int i = 0; i < components.length; i++) {
            GridBagConstraints cons = new GridBagConstraints();
            int gridX = 0;
            int gridY = 0;
            int gridWidth = 1;
            int gridHeight = 1;
            int left = 0;
            int right = 0;
            int top = 0;
            int bottom = 0;
            int x = components[i].getBounds().x;
            int x1 = x + components[i].getBounds().width;
            int y = components[i].getBounds().y;
            int y1 = y + components[i].getBounds().height;
            for (int j = 1; j < axisXnumber + 1; j++) {
                if (x < axisX[j] && x>= axisX[j-1]) {
                    gridX = j-1;
                    left = x - axisX[j-1];
                }
                if (x1 <= axisX[j] && x1 > axisX[j-1]) {
                    gridWidth = j-gridX;
                    right = axisX[j] - x1;
                }
            }
            for (int j = 1; j < axisYnumber+1; j++) {
                if (y < axisY[j] && y >= axisY[j-1]) {
                    gridY = j-1;
                    top = y - axisY[j-1];
                }
                if (y1 <= axisY[j] && y1 > axisY[j-1]) {
                    gridHeight = j-gridY;
                    bottom = axisY[j] - y1;
                }
            }
            // checking whether the preffered size must be adjusted
            cons.ipadx = 0;
            cons.ipady = 0;
            if (components[i].getWidth() > 0)
                cons.ipadx = components[i].getWidth() - components[i].getPreferredSize().width;
            if (components[i].getHeight() > 0)
                cons.ipady = components[i].getHeight() - components[i].getPreferredSize().height;
            // storing calculated values
            cons.gridx = gridX;
            cons.gridy = gridY;
            cons.gridwidth = gridWidth;
            cons.gridheight = gridHeight;
            cons.insets = new Insets(top, left, bottom, right);
            cons.fill = GridBagConstraints.NONE;

            LayoutConstraints lc = new GridBagLayoutConstraints();
            lc.setConstraints(cons);

            cache.put("" + i, lc);
        }
    }

    private static int getCrossings(Component[] components, int axis, int value) {
        int number = 0;
        if (axis == X_AXIS) {
            for (int i = 0; i < components.length; i++) {
                int x = components[i].getBounds().x;
                int x1 = x + components[i].getBounds().width;
                if (x < value && x1 > value)
                    number++;
            }
        } else {
            for (int i = 0; i < components.length; i++) {
                int y = components[i].getBounds().y;
                int y1 = y + components[i].getBounds().height;
                if (y < value && y1 > value)
                    number++;
            }
        }
        return number;
    }
}

/* */
