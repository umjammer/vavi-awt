/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.propertyeditor;

import java.beans.FeatureDescriptor;
import java.util.Comparator;


/**
 * Comparator used to compare java.beans.FeatureDescriptor objects.
 * The Strings returned from getDisplayName are used in the comparison.
 * 
 * @author Mark Davidson
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 1.1 990923 mark original version <br>
 */
public class DescriptorComparator<T extends FeatureDescriptor> implements Comparator<T> {

    /**
     * Compares two FeatureDescriptor objects
     */
    public int compare(T o1, T o2) {
        return o1.getDisplayName().compareTo(o2.getDisplayName());
    }
}

/* */
