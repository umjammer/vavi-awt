/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.util.EventListener;


/**
 * Selection リスナーインターフェースです．
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020603 nsano initial version <br>
 */
public interface SelectionListener extends EventListener {

    /**
     * 属性を変更します．
     */
    void valueChanged(SelectionEvent ev);
}

/* */
