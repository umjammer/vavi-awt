/*
 * Copyright (c) 1997 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.util.logging.Level;

import vavi.awt.BaseImageComponent;
import vavi.util.Debug;


/**
 * JImageComponent.
 *
 * TODO filtered
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 021109 nsano initial version <br>
 */
public class JImageComponent extends BaseImageComponent<BufferedImage> {

    /** Creates an image component. */
    public JImageComponent() {
        super(false);
    }

    /**
     * Creates an image component and make it droppable.
     *
     * @param droppable true: dragging a file into this component is enabled.
     *                  when dropping a file, {@link java.beans.PropertyChangeEvent} named "droppedImage" is fired.
     *                  {@link PropertyChangeEvent#getNewValue()} will be a dropped image.
     */
    public JImageComponent(boolean droppable) {
        super(droppable);
    }

    @Override
    public void setImage(BufferedImage image) {
        super.setImage(image);
        if (image != null) {
            iw = image.getWidth();
            ih = image.getHeight();
Debug.println(Level.FINE, "image: " + iw + "x" + ih + ", " + image);
        }
    }
}

/* */

