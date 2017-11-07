/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Container;
import java.awt.LayoutManager;
import java.beans.PropertyChangeListener;


/**
 * LayoutManagerCustomizer.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 */
public interface LayoutManagerCustomizer {

    /**
     * PropertyChange イベントのリスナーを登録します。
     * @param listener PropertyChange イベントがトリガーされたときに
     *               呼び出されるオブジェクト
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * PropertyChange イベントのリスナーを削除します。
     * @param listener 削除される PropertyChange リスナー
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * カスタマイズする LayoutManager を設定します。
     * このメソッドは、Customizer を親 AWT コンテナに追加する前に
     * 1 回だけ呼び出せます。
     */
    void setObject(LayoutManager layout);

    /** TODO */
    LayoutManager getObject();

    /**
     */
    void setContainer(Container container);

    /**
     */
    void layoutContainer();
}

/* */
