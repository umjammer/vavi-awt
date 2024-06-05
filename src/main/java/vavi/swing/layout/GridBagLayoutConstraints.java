/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * GridBagLayoutConstraints.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 *          0.01 020617 nsano fix firePropertyChange <br>
 */
public class GridBagLayoutConstraints extends LayoutConstraints {

    /** */
    private GridBagConstraints constraints;

    /** Creates layout constraints */
    public GridBagLayoutConstraints() {
    }

    //-------------------------------------------------------------------------

    public int getAnchor() {
        return constraints.anchor;
    }

    public int getFill() {
        return constraints.fill;
    }

    public int getGridheight() {
        return constraints.gridheight;
    }

    public int getGridwidth() {
        return constraints.gridwidth;
    }

    public int getGridx() {
        return constraints.gridx;
    }

    public int getGridy() {
        return constraints.gridy;
    }

    public Insets getInsets() {
        return constraints.insets;
    }

    public int getIpadx() {
        return constraints.ipadx;
    }

    public int getIpady() {
        return constraints.ipady;
    }

    public double getWeightx() {
        return constraints.weightx;
    }

    public double getWeighty() {
        return constraints.weighty;
    }

    public void setAnchor(int anchor) {
        int oldValue = constraints.anchor;
        constraints.anchor = anchor;
        firePropertyChange("anchor", oldValue, anchor);
    }

    public void setFill(int fill) {
        int oldValue = constraints.fill;
        constraints.fill = fill;
        firePropertyChange("fill", oldValue, fill);
    }

    public void setGridheight(int gridheight) {
        int oldValue = constraints.gridheight;
        constraints.gridheight = gridheight;
        firePropertyChange("gridheight", oldValue, gridheight);
    }

    public void setGridwidth(int gridwidth) {
        int oldValue = constraints.gridwidth;
        constraints.gridwidth = gridwidth;
        firePropertyChange("gridwidth", oldValue, gridwidth);
    }

    public void setGridx(int gridx) {
        int oldValue = constraints.gridx;
        constraints.gridx = gridx;
        firePropertyChange("gridx", oldValue, gridx);
    }

    public void setGridy(int gridy) {
        int oldValue = constraints.gridy;
        constraints.gridy = gridy;
        firePropertyChange("gridy", oldValue, gridy);
    }

    public void setInsets(Insets insets) {
        Insets oldValue = constraints.insets;
        constraints.insets = insets;
        firePropertyChange("insets", oldValue, insets);
    }

    public void setIpadx(int ipadx) {
        int oldValue = constraints.ipadx;
        constraints.ipadx = ipadx;
        firePropertyChange("ipadx", oldValue, ipadx);
    }

    public void setIpady(int ipady) {
        int oldValue = constraints.ipady;
        constraints.ipady = ipady;
        firePropertyChange("ipady", oldValue, ipady);
    }

    public void setWeightx(double weightx) {
        Double oldValue = constraints.weightx;
        constraints.weightx = weightx;
        firePropertyChange("weightx", oldValue, weightx);
    }

    public void setWeighty(double weighty) {
        Double oldValue = constraints.weighty;
        constraints.weighty = weighty;
        firePropertyChange("weighty", oldValue, weighty);
    }

    // LayoutConstraints ------------------------------------------------------

    /**
     * gets constraints.
     */
    public Object getConstraints() {
        return constraints;
    }

    /**
     * gets constraints.
     */
    public void setConstraints(Object constraints) {
        this.constraints = (GridBagConstraints) constraints;
    }
}
