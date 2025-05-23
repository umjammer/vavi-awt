/*
 * Copyright (c) 1997 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import static java.lang.System.getLogger;


/**
 * ImageComponent.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 220919 nsano initial version <br>
 */
public class ImageComponent extends BaseImageComponent<Image> {

    private static final Logger logger = getLogger(ImageComponent.class.getName());

    /** Creates an image component. */
    public ImageComponent() {
        super(false);
    }

    /**
     * Creates an image component and make it droppable.
     *
     * @param droppable true: dragging a file into this component is enabled.
     *                  when dropping a file, {@link PropertyChangeEvent} named "droppedImage" is fired.
     *                  {@link PropertyChangeEvent#getNewValue()} will be a dropped image.
     */
    public ImageComponent(boolean droppable) {
        super(droppable);
    }

    @Override
    public void setImage(Image image) {
        super.setImage(image);
        if (image != null) {
            iw = image.getWidth(null);
            ih = image.getHeight(null);
logger.log(Level.DEBUG, "image: " + iw + "x" + ih + ", " + image);
        }
    }
}
