/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

/**
 * Editable.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020510 nsano initial version <br>
 */
public interface Editable {
    /** */
    void cut();

    /** */
    void copy();

    /** */
    void paste();

    /** */
    void delete();

    /** */
    void selectAll();
}
