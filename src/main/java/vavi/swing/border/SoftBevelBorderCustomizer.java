/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;


/**
 * SoftBevelBorderCustomizer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020525 nsano initial version <br>
 */
public class SoftBevelBorderCustomizer extends BasicBorderCustomizer {

    private SoftBevelBorder softBevel;

    /** */
    public SoftBevelBorderCustomizer() {
        setObject(new SoftBevelBorder(BevelBorder.RAISED));
        tableModel.addPropertyChangeListener(pcl);
    }

    /** */
    private PropertyChangeListener pcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {
            int type = softBevel.getBevelType();
            Color hi = softBevel.getHighlightInnerColor();
            Color ho = softBevel.getHighlightOuterColor();
            Color si = softBevel.getShadowInnerColor();
            Color so = softBevel.getShadowOuterColor();

            String name = ev.getPropertyName();
            if ("bevelType".equals(name)) {
                type = (Integer) ev.getNewValue();
            } else if ("highlightInnerColor".equals(name)) {
                hi = (Color) ev.getNewValue();
            } else if ("highlightOuterColor".equals(name)) {
                ho = (Color) ev.getNewValue();
            } else if ("shadowInnerColor".equals(name)) {
                si = (Color) ev.getNewValue();
            } else if ("shadowOuterColor".equals(name)) {
                so = (Color) ev.getNewValue();
            }

            softBevel = new SoftBevelBorder(type, ho, hi, so, si);
            borderSample.setBorder(softBevel);

            SoftBevelBorderCustomizer.super.setObject(softBevel);
        }
    };

    /** */
    public void setObject(Border border) {
        int type = ((SoftBevelBorder) border).getBevelType();
        Color hi = ((SoftBevelBorder) border).getHighlightInnerColor();
        Color ho = ((SoftBevelBorder) border).getHighlightOuterColor();
        Color si = ((SoftBevelBorder) border).getShadowInnerColor();
        Color so = ((SoftBevelBorder) border).getShadowOuterColor();

        softBevel = new SoftBevelBorder(type, ho, hi, so, si);
        borderSample.setBorder(softBevel);

        super.setObject(border);
    }
}
