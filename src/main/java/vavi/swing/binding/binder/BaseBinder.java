/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binding.binder;

import vavi.swing.binding.Binder;
import vavi.swing.binding.Updater;


/**
 * BaseBinder.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-14 nsano initial version <br>
 */
public abstract class BaseBinder<T> implements Binder<T> {

    /** for make sure once */
    protected boolean enabled = true;

    @Override
    public void setEnabled(boolean isEnabled) {
        this.enabled = isEnabled;
    }

    /** */
    protected Updater<T> updater;

    @Override
    public void setUpdater(Updater<T> updater) {
        this.updater = updater;
    }
}
