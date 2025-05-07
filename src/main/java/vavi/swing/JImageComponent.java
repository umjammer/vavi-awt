/*
 * Copyright (c) 1997 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import vavi.awt.BaseImageComponent;

import static java.lang.System.getLogger;


/**
 * JImageComponent.
 *
 * TODO filtered
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 021109 nsano initial version <br>
 */
public class JImageComponent extends BaseImageComponent<BufferedImage> {

    private static final Logger logger = getLogger(JImageComponent.class.getName());

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
logger.log(Level.DEBUG, "image: " + iw + "x" + ih + ", " + image);
        }
    }
}
