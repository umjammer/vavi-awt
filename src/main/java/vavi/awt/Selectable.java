/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt;


/**
 * Selectable interfaces.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020510 nsano initial version <br>
 */
public interface Selectable {

    /**
     * Sets the selection state.
     */
    void setSelected(boolean isSelected);

    /**
     * Returns the selection state.
     */
    boolean isSelected();
}
