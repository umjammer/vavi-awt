/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;


/**
 * CompoundBorderCustomizer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class CompoundBorderCustomizer extends BasicBorderCustomizer {

    private CompoundBorder compound;

    /** */
    public CompoundBorderCustomizer() {
        setObject(BorderFactory.createCompoundBorder());
        tableModel.addPropertyChangeListener(pcl);
    }

    /** */
    private PropertyChangeListener pcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {
            Border inside  = compound.getInsideBorder();
            Border outside = compound.getOutsideBorder();

            String name = ev.getPropertyName();
            if ("insideBorder".equals(name)) {
                inside = (Border) ev.getNewValue();
            } else if ("outsideBorder".equals(name)) {
                outside = (Border) ev.getNewValue();
            }

            compound = BorderFactory.createCompoundBorder(inside, outside);
            borderSample.setBorder(compound);

            CompoundBorderCustomizer.super.setObject(compound);
        }
    };

    /** */
    public void setObject(Border border) {
        Border inside  = ((CompoundBorder) border).getInsideBorder();
        Border outside = ((CompoundBorder) border).getOutsideBorder();

        compound = BorderFactory.createCompoundBorder(inside, outside);
        borderSample.setBorder(compound);

        super.setObject(border);
    }
}

/* */
