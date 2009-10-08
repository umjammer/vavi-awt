/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 * NullLayoutConverter.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020617 nsano initial version <br>
 *          0.01 020617 nsano fix firePropertyChange <br>
 */
public class NullLayoutConverter {

    /** */
//  private boolean savingAsGridBagLayout;
    /** */
    private boolean overlaid;
    /** */
    private boolean justifyGrid;
    /** */
    private Dimension gridSize;

    /** */
    public NullLayoutConverter() {
//      savingAsGridBagLayout = false;
        overlaid = true;
        justifyGrid = false;
        gridSize = new Dimension(20, 20);
    }

//  public boolean isSavingAsGridBagLayout() {
//  	return savingAsGridBagLayout;
//  }

    public boolean isOverlaid() {
        return overlaid;
    }

    public boolean isJustifyGrid() {
        return justifyGrid;
    }

    public Dimension getGridSize() {
        return gridSize;
    }

//  public void setSavingAsGridBagLayout(boolean savingAsGridBagLayout) {
//      boolean oldValue = this.savingAsGridBagLayout;
//  	this.savingAsGridBagLayout = savingAsGridBagLayout;
//      firePropertyChange("savingAsGridBagLayout",
//                         oldValue,
//                         savingAsGridBagLayout);
//  }

    public void setOverlaid(boolean overlaid) {
        boolean oldValue = this.overlaid;
        this.overlaid = overlaid;
        firePropertyChange("overlaid", oldValue, overlaid);
    }

    public void setJustifyGrid(boolean justifyGrid) {
        boolean oldValue = this.justifyGrid;
        this.justifyGrid = justifyGrid;
        firePropertyChange("justifyGrid", oldValue, justifyGrid);
    }

    public void setGridSize(Dimension gridSize) {
        Dimension oldValue = this.gridSize;
        this.gridSize = gridSize;
        firePropertyChange("gridSize", oldValue, gridSize);
    }

    // -------------------------------------------------------------------------

    /** */
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /** */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /** */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /** */
    protected void firePropertyChange(String name,
                                      boolean oldValue, boolean newValue) {
        pcs.firePropertyChange(name, oldValue, newValue);
    }

    /** */
    protected void firePropertyChange(String name,
                                      Object oldValue, Object newValue) {
        pcs.firePropertyChange(name, oldValue, newValue);
    }
}

/* */
