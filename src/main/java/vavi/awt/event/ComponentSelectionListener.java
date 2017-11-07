/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.util.EventListener;


/**
 * ComponentSelection リスナーインターフェースです．
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020509 nsano initial version <br>
 */
public interface ComponentSelectionListener extends EventListener {

    /**
     * 属性を変更します．
     */
    void valueChanged(ComponentSelectionEvent ev);
}

/* */
