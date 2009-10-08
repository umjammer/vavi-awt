/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.beans.PropertyChangeListener;

import javax.swing.border.Border;


/**
 * BorderCustomizer.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 *          1.00 020527 nsano complete <br>
 */
public interface BorderCustomizer /* extends Customizer */ {

    /**
     * PropertyChange イベントのリスナーを登録します。
     * @param listener PropertyChange イベントがトリガーされたときに
     *		       呼び出されるオブジェクト
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * PropertyChange イベントのリスナーを削除します。
     * @param listener 削除される PropertyChange リスナー
     */
    void removePropertyChangeListener(PropertyChangeListener listener);
 
    /**
     * カスタマイズする Border を設定します。
     * このメソッドは、Customizer を親 AWT コンテナに追加する前に
     * 1 回だけ呼び出せます。
     */
    void setObject(Border border);

    /** TODO TabbedPane の ChangeEvent のためだけ */
    Border getObject();
}

/* */
