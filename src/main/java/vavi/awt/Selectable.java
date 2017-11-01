/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt;


/**
 * 選択可能であるインターフェース．
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020510 nsano initial version <br>
 */
public interface Selectable {

    /**
     * 選択状態を設定します．
     */
    void setSelected(boolean isSelected);

    /**
     * 選択状態を返します．
     */
    boolean isSelected();
}

/* */

