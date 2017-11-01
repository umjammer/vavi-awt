/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Container;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;


/**
 * AbstractConstraintEditor.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 *          0.01 020617 nsano refine <br>
 */
public abstract class AbstractLayoutConstraintsEditor {

    /** current component no. */
    protected int index = -1;

    /** target container */
    protected Container container;

    /** key: no. of component, value: LayoutConstraints */
    protected Map<String,LayoutConstraints> cache = new HashMap<>();

    /** target layout manager */
    protected LayoutManager2 layout;

    /** Creates layout constraints editor */
    public AbstractLayoutConstraintsEditor(LayoutManager2 layout) {
        this.layout = layout;
    }

    //-------------------------------------------------------------------------

    /** sets container */
    public void setContainer(Container container) {
        this.container = container;
    }

    /** change target index to edit */
    public void setCurrentIndex(int index) {
        this.index = index;
    }

    /** gets target index to edit */
    public int getCurrentIndex() {
        return index;
    }

    /** gets target constraint */
    public LayoutConstraints getLayoutConstraints(int index) {
        return cache.get("" + index);
    }

    /** sets target constraint */
    public void setLayoutConstraints(int index, LayoutConstraints constraints) {
        cache.put(String.valueOf(index), constraints);
    }

    /** TODO clear editor */
    public void clear() {
        cache.clear();
    }
}

/* */
