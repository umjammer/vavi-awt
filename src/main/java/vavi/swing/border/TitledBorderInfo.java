/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.awt.*;
import java.beans.*;
import javax.swing.border.*;


/**
 * TitledBorderInfo.
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
public class TitledBorderInfo extends SimpleBorderInfo {

    private final Class<?> clazz = TitledBorder.class;
    private final Class<?> customizerClass = TitledBorderCustomizer.class;

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, customizerClass);
    }

    @Override
    public Image getIcon(int iconKind) {
        return loadImage("resources/titledBorder.gif");
    }

    @Override
    public BorderPropertyDescriptor[] getBorderPropertyDescriptors() {
        BorderPropertyDescriptor[] bpds = new BorderPropertyDescriptor[6];
        bpds[0] = new BorderPropertyDescriptor("border", clazz);
        bpds[1] = new BorderPropertyDescriptor("title", clazz);
        bpds[2] = new BorderPropertyDescriptor("titleJustification", clazz);
        Object[] value = new Object[] {
            "DEFAULT_JUSTIFICATION", TitledBorder.DEFAULT_JUSTIFICATION, null,
            "LEFT", TitledBorder.LEFT, null,
            "CENTER", TitledBorder.CENTER, null,
            "RIGHT", TitledBorder.RIGHT, null,
            "LEADING", TitledBorder.LEADING, null,
            "TRAILING", TitledBorder.TRAILING, null,
        };
        bpds[2].setValue("enumerationValues", value);
        bpds[3] = new BorderPropertyDescriptor("titlePosition", clazz);
        value = new Object[] {
            "DEFAULT_POSITION", TitledBorder.DEFAULT_POSITION, null,
            "ABOVE_TOP", TitledBorder.ABOVE_TOP, null,
            "TOP", TitledBorder.TOP, null,
            "BELOW_TOP", TitledBorder.BELOW_TOP, null,
            "ABOVE_BOTTOM", TitledBorder.ABOVE_BOTTOM, null,
            "BOTTOM", TitledBorder.BOTTOM, null,
            "BELOW_BOTTOM", TitledBorder.BELOW_BOTTOM, null,
        };
        bpds[3].setValue("enumerationValues", value);
        bpds[4] = new BorderPropertyDescriptor("titleFont", clazz);
        bpds[5] = new BorderPropertyDescriptor("titleColor", clazz);

        return bpds;
    }
}
