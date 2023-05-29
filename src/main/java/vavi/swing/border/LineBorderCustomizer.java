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
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


/**
 * LineBorderCustomizer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class LineBorderCustomizer extends BasicBorderCustomizer {

    private LineBorder line;

    /** */
    public LineBorderCustomizer() {
        setObject(BorderFactory.createLineBorder(Color.black));
        tableModel.addPropertyChangeListener(pcl);
    }

    /** */
    private PropertyChangeListener pcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {
            Color   c  = line.getLineColor();
            int     t  = line.getThickness();
            boolean rc = line.getRoundedCorners();

            String name = ev.getPropertyName();
            if ("lineColor".equals(name)) {
                c = (Color) ev.getNewValue();
            } else if ("thickness".equals(name)) {
                t = (Integer) ev.getNewValue();
            } else if ("roundedCorners".equals(name)) {
                rc = (Boolean) ev.getNewValue();
            }

            line = new LineBorder(c, t, rc);
            borderSample.setBorder(line);

            LineBorderCustomizer.super.setObject(line);
        }
    };

    /** */
    public void setObject(Border border) {
        Color   c  = ((LineBorder) border).getLineColor();
        int     t  = ((LineBorder) border).getThickness();
        boolean rc = ((LineBorder) border).getRoundedCorners();

        line = new LineBorder(c, t, rc);
        borderSample.setBorder(line);

        super.setObject(border);
    }
}

/* */
