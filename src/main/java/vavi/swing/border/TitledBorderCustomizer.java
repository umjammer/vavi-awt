/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


/**
 * TitledBorderCustomizer.
 *
 * @beaninfo Border "border"
 * @beaninfo String "title"
 * @beaninfo int "titleJustification" true
 *  { DEFAULT_JUSTIFICATION, ABOVE_BOTTOM, ABOVE_TOP, BELOW_BOTTOM, BELOW_TOP }
 * @beaninfo int "titlePosition" true
 *  { DEFAULT_POSITION, BOTTOM, CENTER, LEADING, LEFT, RIGHT, TOP, TRAILING }
 * @beaninfo Font "titleFont"
 * @beaninfo Color "titleColor"
 *
 * TODO たぶん bean として機能する．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020524 nsano initial version <br>
 */
public class TitledBorderCustomizer extends BasicBorderCustomizer {

    private TitledBorder titled;

    /** */
    public TitledBorderCustomizer() {
        setObject(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "title"));
        tableModel.addPropertyChangeListener(pcl);
    }

    /** */
    private PropertyChangeListener pcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {

            String name = ev.getPropertyName();
            if ("border".equals(name)) {
                titled.setBorder((Border) ev.getNewValue());
            } else if ("title".equals(name)) {
                titled.setTitle((String) ev.getNewValue());
            } else if ("titleJustification".equals(name)) {
                int tj = (Integer) ev.getNewValue();
                titled.setTitleJustification(tj);
            } else if ("titlePosition".equals(name)) {
                int tp = (Integer) ev.getNewValue();
                titled.setTitlePosition(tp);
            } else if ("titleFont".equals(name)) {
                titled.setTitleFont((Font) ev.getNewValue());
            } else if ("titleColor".equals(name)) {
                titled.setTitleColor((Color) ev.getNewValue());
            }

            borderSample.repaint();

            TitledBorderCustomizer.super.setObject(titled);
        }
    };

    /** */
    public void setObject(Border border) {
        Border b = ((TitledBorder) border).getBorder();
        String t = ((TitledBorder) border).getTitle();
        int tj = ((TitledBorder) border).getTitleJustification();
        int tp = ((TitledBorder) border).getTitlePosition();
        Font f = ((TitledBorder) border).getTitleFont();
        Color c = ((TitledBorder) border).getTitleColor();

        titled = BorderFactory.createTitledBorder(b, t, tj, tp, f, c);
        borderSample.setBorder(titled);

        super.setObject(border);
    }
}
