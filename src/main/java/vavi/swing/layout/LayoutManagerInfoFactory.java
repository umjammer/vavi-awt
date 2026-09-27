/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.beans.BeanInfo;
import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.System.getLogger;


/**
 * Since the LayoutManager class cannot be handled by the Bean specification,
 * this class is used instead of the Introspector to obtain the BeanInfo of *LayoutManager.
 * <p>
 * {@link LayoutManagerInfo}s are found by {@link ServiceLoader},
 * register yours in {@code META-INF/services/vavi.swing.layout.LayoutManagerInfo}.
 * </p>
 *
 * @depends layoutManager.xml
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 *          1.00 020527 nsano refine <br>
 *          1.10 260925 nsano use ServiceLoader, XMLDecoder <br>
 */
public class LayoutManagerInfoFactory {

    private static final Logger logger = getLogger(LayoutManagerInfoFactory.class.getName());

    /** */
    private LayoutManagerInfoFactory() {}

    /** key: layout manager class */
    private static final Map<Class<?>, ServiceLoader.Provider<LayoutManagerInfo>> providers =
            ServiceLoader.load(LayoutManagerInfo.class).stream()
                    .collect(Collectors.toMap(p -> p.get().getBeanDescriptor().getBeanClass(), Function.identity(), (a, b) -> a));

    /**
     * @return null when not found
     */
    public static BeanInfo getBeanInfo(Class<?> lmClass) {
        ServiceLoader.Provider<LayoutManagerInfo> provider = providers.get(lmClass);
        if (provider == null) {
logger.log(Level.ERROR, "no LayoutManagerInfo for: " + lmClass.getName());
            return null;
        }
        return provider.get();
    }

    // ----

    /**
     * returns a new list every time.
     * <ul>
     * <li>first: null layout</li>
     * <li>last: user defined</li>
     * </ul>
     */
    @SuppressWarnings("unchecked")
    public static List<SampleLayoutManagerInfo> getSampleLayoutManagerInfos() {
        try (InputStream is = LayoutManagerInfoFactory.class.getResourceAsStream("layoutManager.xml");
             XMLDecoder decoder = new XMLDecoder(is, null, e -> { throw new IllegalStateException(e); })) {
            return (List<SampleLayoutManagerInfo>) decoder.readObject();
        } catch (IOException e) {
logger.log(Level.ERROR, e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }
}
