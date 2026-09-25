/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.beans.BeanInfo;


/**
 * BeanInfo for LayoutManager class.
 * <p>
 * this is the service type for {@link java.util.ServiceLoader},
 * implementations are listed in {@code META-INF/services/vavi.swing.layout.LayoutManagerInfo}.
 * {@link #getBeanDescriptor()}{@code .getBeanClass()} must return the target LayoutManager class.
 * </p>
 * <p>
 * please obtain the LayoutManagerInfo class using LayoutManagerInfoFactory instead of Introspector.
 * </p>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 260925 nsano initial version <br>
 */
public interface LayoutManagerInfo extends BeanInfo {
}
