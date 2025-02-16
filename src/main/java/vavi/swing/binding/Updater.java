/*
 * Copyright (c) 2017 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding;


/**
 * Updater.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 Oct 29, 2017 umjammer initial version <br>
 */
public interface Updater<T> {

    /** @param arg bean */
    void update(T arg);
}
