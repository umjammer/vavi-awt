/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


/**
 * EmptyBorderCustomizer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class EmptyBorderCustomizer extends BasicBorderCustomizer {

    private Border empty;

    /** */
    public EmptyBorderCustomizer() {
        setObject(BorderFactory.createEmptyBorder());
        tableModel.addPropertyChangeListener(pcl);
    }

    /** */
    private PropertyChangeListener pcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {
            Insets i = ((EmptyBorder) empty).getBorderInsets();

            String name = ev.getPropertyName();
            if ("borderInsets".equals(name)) {
                i = (Insets) ev.getNewValue();
            }

            empty =
             BorderFactory.createEmptyBorder(i.top, i.left, i.bottom, i.right);
            borderSample.setBorder(empty);

            EmptyBorderCustomizer.super.setObject(empty);
        }
    };

    /** */
    public void setObject(Border border) {
        Insets i = ((EmptyBorder) border).getBorderInsets();

        empty =
            BorderFactory.createEmptyBorder(i.top, i.left, i.bottom, i.right);
        borderSample.setBorder(empty);

        super.setObject(border);
    }
}

/* */
