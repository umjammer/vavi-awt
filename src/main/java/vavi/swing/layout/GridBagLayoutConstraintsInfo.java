/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * GridBagLayoutConstraintsInfo.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020528 nsano initial version <br>
 */
public class GridBagLayoutConstraintsInfo extends SimpleBeanInfo {

    private final Class<?> clazz = GridBagLayoutConstraints.class;

    /** */
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, null);
    }

    /** */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor[] pds = new PropertyDescriptor[11];

            pds[0] = new PropertyDescriptor("anchor", clazz);
            Object[] value = new Object[] {
                "CENTER", GridBagConstraints.CENTER, null,
                "EAST", GridBagConstraints.EAST, null,
                "NORTH", GridBagConstraints.NORTH, null,
                "NORTHEAST", GridBagConstraints.NORTHEAST, null,
                "NORTHWEST", GridBagConstraints.NORTHWEST, null,
                "SOUTH", GridBagConstraints.SOUTH, null,
                "SOUTHEAST", GridBagConstraints.SOUTHEAST, null,
                "SOUTHWEST", GridBagConstraints.SOUTHWEST, null,
                "WEST", GridBagConstraints.WEST, null,
            };
            pds[0].setValue("enumerationValues", value);

            pds[1] = new PropertyDescriptor("fill", clazz);
            value = new Object[] {
                "NONE", GridBagConstraints.NONE, null,
                "HORIZONTAL", GridBagConstraints.HORIZONTAL, null,
                "VERTICAL", GridBagConstraints.VERTICAL, null,
                "BOTH", GridBagConstraints.BOTH, null,
            };
            pds[1].setValue("enumerationValues", value);

            pds[2] = new PropertyDescriptor("gridheight", clazz);
            value = new Object[] {
                "RELATIVE", GridBagConstraints.RELATIVE, null,
                "REMAINDER", GridBagConstraints.REMAINDER, null,
            };
            pds[2].setValue("enumerationValuesPlus", value);
            pds[2].setValue("minimumValue", 0);

            pds[3] = new PropertyDescriptor("gridwidth", clazz);
            value = new Object[] {
                "RELATIVE", GridBagConstraints.RELATIVE, null,
                "REMAINDER", GridBagConstraints.REMAINDER, null,
            };
            pds[3].setValue("enumerationValuesPlus", value);
            pds[3].setValue("minimumValue", 0);

            pds[4] = new PropertyDescriptor("gridx", clazz);
            value = new Object[] {
                "RELATIVE", GridBagConstraints.RELATIVE, null,
            };
            pds[4].setValue("enumerationValuesPlus", value);
            pds[4].setValue("minimumValue", 0);

            pds[5] = new PropertyDescriptor("gridy", clazz);
            value = new Object[] {
                "RELATIVE", GridBagConstraints.RELATIVE, null,
            };
            pds[5].setValue("enumerationValuesPlus", value);
            pds[5].setValue("minimumValue", 0);

            pds[6] = new PropertyDescriptor("insets", clazz);
            pds[6].setValue("defaultValue", new Insets(0, 0, 0, 0));

            pds[7] = new PropertyDescriptor("ipadx", clazz);

            pds[8] = new PropertyDescriptor("ipady", clazz);

            pds[9] = new PropertyDescriptor("weightx", clazz);
            pds[9].setValue("minimumValue", (double) 0);

            pds[10] = new PropertyDescriptor("weighty", clazz);
            pds[10].setValue("minimumValue", (double) 0);

            return pds;
        } catch (IntrospectionException e) {
Debug.println(Level.SEVERE, e);
            throw new IllegalStateException(e);
        }
    }
}
