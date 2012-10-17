/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.beans.*;


/**
 * LayoutManager の Constraints クラスを Beans として扱う
 * ラッパークラスを作成するための基底クラスです．
 * <p>
 * setter メソッドでは PropertyChangeEvent を発行してください． // TODO
 * </p>
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 */
public abstract class LayoutConstraints {

    /**
     * Gets constraints.
     */
    public abstract Object getConstraints();

    /**
     * Sets constraints.
     */
    public abstract void setConstraints(Object constraints);

    //-------------------------------------------------------------------------

    /** PropertyChange イベント機構のユーティリティ */
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /** PropertyChangeListener を追加します． */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /** PropertyChangeListener を削除します． */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /** PropertyChangeEvent を発行します． */
    protected void firePropertyChange(String name,
                                      int oldValue, int newValue) {
        pcs.firePropertyChange(name, oldValue, newValue);
    }

    /** PropertyChangeEvent を発行します． */
    protected void firePropertyChange(String name,
                                      Object oldValue, Object newValue) {
        pcs.firePropertyChange(name, oldValue, newValue);
    }
}

/* */
