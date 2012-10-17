/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt;

import java.awt.Component;


/**
 * コントローラです。
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020605 nsano initial version <br>
 *          0.01 020611 nsano change package <br>
 */
public interface Controller extends Selectable {

    /** */
    void setView(Component view);

    /** */
    Component getView();
}

/* */
