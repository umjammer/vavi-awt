/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

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
 * BorderInfoFactory.
 * <p>
 * {@link BorderInfo}s are found by {@link ServiceLoader},
 * register yours in {@code META-INF/services/vavi.swing.border.BorderInfo}.
 * </p>
 *
 * @depends border.xml
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 *          1.00 020527 nsano refine <br>
 *          1.10 260925 nsano use ServiceLoader, XMLDecoder <br>
 */
public class BorderInfoFactory {

    private static final Logger logger = getLogger(BorderInfoFactory.class.getName());

    /** */
    private BorderInfoFactory() {}

    /** key: border class */
    private static final Map<Class<?>, ServiceLoader.Provider<BorderInfo>> providers =
            ServiceLoader.load(BorderInfo.class).stream()
                    .collect(Collectors.toMap(p -> p.get().getBeanDescriptor().getBeanClass(), Function.identity(), (a, b) -> a));

    /**
     * @return null when not found
     */
    public static BorderInfo getBorderInfo(Class<?> borderClass) {
        ServiceLoader.Provider<BorderInfo> provider = providers.get(borderClass);
        if (provider == null) {
logger.log(Level.ERROR, "no BorderInfo for: " + borderClass.getName());
            return null;
        }
        return provider.get();
    }

    // ----

    /**
     * returns a new list every time.
     * <ul>
     * <li>first: null border</li>
     * <li>last: user defined</li>
     * </ul>
     */
    @SuppressWarnings("unchecked")
    public static List<SampleBorderInfo> getSampleBorderInfos() {
        try (InputStream is = BorderInfoFactory.class.getResourceAsStream("border.xml");
             XMLDecoder decoder = new XMLDecoder(is, null, e -> { throw new IllegalStateException(e); })) {
            return (List<SampleBorderInfo>) decoder.readObject();
        } catch (IOException e) {
logger.log(Level.ERROR, e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }
}
