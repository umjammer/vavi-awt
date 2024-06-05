/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Vector;


/**
 * Action リスナーのユーティリティです．
 *
 * @target 1.1
 * @caution JDK 1.1 用に書かれているので swing は使っちゃいかん！
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020503 nsano initial version <br>
 *          0.01 020914 nsano TODO synchronized <br>
 */
public class ActionSupport implements Serializable {

    /** Action のリスナー */
    private final Vector<ActionListener> listeners = new Vector<>();

    /**
     * Action リスナーをアタッチします．
     *
     * @param l ActionListener
     */
    public void addActionListener(ActionListener l) {
        listeners.addElement(l);
    }

    /**
     * Action リスナーをリムーブします．
     *
     * @param l ActionListener
     */
    public void removeActionListener(ActionListener l) {
        listeners.removeElement(l);
    }

    /**
     * イベントを発行します．
     */
    public synchronized void fireActionPerformed(ActionEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.elementAt(i).actionPerformed(ev);
        }
    }
}
