/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;


/**
 * BevelBorderCustomizer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class BevelBorderCustomizer extends BasicBorderCustomizer {

    private Border bevel;

    /** */
    public BevelBorderCustomizer() {
        setObject(BorderFactory.createRaisedBevelBorder());
        tableModel.addPropertyChangeListener(pcl);
    }

    /** */
    private PropertyChangeListener pcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {
            int type = ((BevelBorder) bevel).getBevelType();
            Color hi = ((BevelBorder) bevel).getHighlightInnerColor();
            Color ho = ((BevelBorder) bevel).getHighlightOuterColor();
            Color si = ((BevelBorder) bevel).getShadowInnerColor();
            Color so = ((BevelBorder) bevel).getShadowOuterColor();

            String name = ev.getPropertyName();
            if ("bevelType".equals(name)) {
                type = (Integer) ev.getNewValue();
            }
            else if ("highlightInnerColor".equals(name)) {
                hi = (Color) ev.getNewValue();
            }
            else if ("highlightOuterColor".equals(name)) {
                ho = (Color) ev.getNewValue();
            }
            else if ("shadowInnerColor".equals(name)) {
                si = (Color) ev.getNewValue();
            }
            else if ("shadowOuterColor".equals(name)) {
                so = (Color) ev.getNewValue();
            }

            bevel = BorderFactory.createBevelBorder(type, ho, hi, so, si);
            borderSample.setBorder(bevel);

            BevelBorderCustomizer.super.setObject(bevel);
        }
    };

    /** */
    public void setObject(Border border) {
        int type = ((BevelBorder) border).getBevelType();
        Color hi = ((BevelBorder) border).getHighlightInnerColor();
        Color ho = ((BevelBorder) border).getHighlightOuterColor();
        Color si = ((BevelBorder) border).getShadowInnerColor();
        Color so = ((BevelBorder) border).getShadowOuterColor();

        bevel = BorderFactory.createBevelBorder(type, ho, hi, so, si);
        borderSample.setBorder(bevel);

        super.setObject(border);
    }
}

/* */
