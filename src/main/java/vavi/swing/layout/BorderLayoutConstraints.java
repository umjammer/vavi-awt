/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;


/**
 * BorderLayoutConstraints.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 *          0.01 020617 nsano fix firePropertyChange <br>
 */
public class BorderLayoutConstraints extends LayoutConstraints {

    /** constraints */
    private String constraints;

    //-------------------------------------------------------------------------

    /** for table model setter */
    public void setDirection(String direction) {
        String oldValue = this.constraints;
        this.constraints = direction;
        firePropertyChange("direction", oldValue, direction);
    }

    /** for table model getter */
    public String getDirection() {
        return constraints;
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
        this.constraints = (String) constraints;
    }
}

/* */
