/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.border.*;


/**
 * EtchedBorderCustomizer.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class EtchedBorderCustomizer extends BasicBorderCustomizer {

    private Border etched;

    /** */
    public EtchedBorderCustomizer() {
        setObject(BorderFactory.createEtchedBorder());
        tableModel.addPropertyChangeListener(pcl);
    }

    /** */
    private PropertyChangeListener pcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {
            int type = ((EtchedBorder) etched).getEtchType();
            Color h  = ((EtchedBorder) etched).getHighlightColor();
            Color s  = ((EtchedBorder) etched).getShadowColor();

            String name = ev.getPropertyName();
            if ("etchType".equals(name)) {
                type = ((Integer) ev.getNewValue()).intValue();
            }
            else if ("highlightColor".equals(name)) {
                h = (Color) ev.getNewValue();
            }
            else if ("shadowColor".equals(name)) {
                s = (Color) ev.getNewValue();
            }

            etched = BorderFactory.createEtchedBorder(type, h, s);
            borderSample.setBorder(etched);

            EtchedBorderCustomizer.super.setObject(etched);
        }
    };

    /** */
    public void setObject(Border border) {
        int type = ((EtchedBorder) border).getEtchType();
        Color h  = ((EtchedBorder) border).getHighlightColor();
        Color s  = ((EtchedBorder) border).getShadowColor();

        etched = BorderFactory.createEtchedBorder(type, h, s);
        borderSample.setBorder(etched);

        super.setObject(border);
    }
}

/* */
