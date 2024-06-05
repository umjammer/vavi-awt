/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Container;

import javax.swing.OverlayLayout;


/**
 * OverlayLayoutCustomizer.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 */
public class OverlayLayoutCustomizer extends BasicLayoutManagerCustomizer {

    /** */
    public OverlayLayoutCustomizer() {
    }

    @Override
    public void setContainer(Container container) {
        super.setContainer(container);
        setObject(new OverlayLayout(container));
    }
}
