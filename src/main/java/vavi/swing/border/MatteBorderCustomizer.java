/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Color;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;


/**
 * MatteBorderCustomizer.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 *          0.01 020603 nsano fix icon/color null bug <br>
 */
public class MatteBorderCustomizer extends BasicBorderCustomizer {

    private MatteBorder matte;

    /** */
    public MatteBorderCustomizer() {
        setObject(BorderFactory.createMatteBorder(1, 10, 1, 1, Color.red));
        tableModel.addPropertyChangeListener(pcl);
    }

    /** */
    private PropertyChangeListener pcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {
            Color  color  = matte.getMatteColor();
            Icon   icon   = matte.getTileIcon();
            Insets i      = matte.getBorderInsets();

            String name = ev.getPropertyName();
            if ("matteColor".equals(name)) {
                icon = null;
                color = (Color) ev.getNewValue();
            } else if ("tileIcon".equals(name)) {
                icon = (Icon) ev.getNewValue();
                color = null;
            } else if ("borderInsets".equals(name)) {
                i = (Insets) ev.getNewValue();
            }

            if (icon != null) {
                matte = BorderFactory.
                    createMatteBorder(i.top, i.left, i.bottom, i.right, icon);
            } else {
                matte = BorderFactory.
                    createMatteBorder(i.top, i.left, i.bottom, i.right, color);
            }
            borderSample.setBorder(matte);

            MatteBorderCustomizer.super.setObject(matte);
        }
    };

    /** */
    public void setObject(Border border) {
        Color  color  = ((MatteBorder) border).getMatteColor();
        Icon   icon   = ((MatteBorder) border).getTileIcon();
        Insets i      = ((MatteBorder) border).getBorderInsets();

        if (icon != null) {
            matte = BorderFactory.
                createMatteBorder(i.top, i.left, i.bottom, i.right, icon);
        } else {
            matte = BorderFactory.
                createMatteBorder(i.top, i.left, i.bottom, i.right, color);
        }
        borderSample.setBorder(matte);

        super.setObject(border);
    }
}

/* */
