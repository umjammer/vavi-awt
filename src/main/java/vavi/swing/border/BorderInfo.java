/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.beans.BeanInfo;


/**
 * BeanInfo for Border class.
 * <p>
 * Since most Border classes do not have a getter method,
 * we prepare and use a BorderPropertyDescriptor as a PropertyDescriptor that only handles the getter method.
 * </p>
 * <p>
 * Since this differs from the Beans specification,
 * please obtain the BorderInfo class using BorderInfoFactory instead of Introspector.
 * </p>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 */
public interface BorderInfo extends BeanInfo {

    /** */
    BorderPropertyDescriptor[] getBorderPropertyDescriptors();
}
