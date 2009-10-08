/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;


/**
 * CardLayoutConstraints.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020614 nsano initial version <br>
 */
public class CardLayoutConstraints extends LayoutConstraints {

    /** constraints */
    private String constraints;

    //-------------------------------------------------------------------------

    /** for table model setter */
    public void setTitle(String title) {
        String oldValue = this.constraints;
        this.constraints = title;
        firePropertyChange("title", oldValue, title);
    }

    /** for table model getter */
    public String getTitle() {
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
