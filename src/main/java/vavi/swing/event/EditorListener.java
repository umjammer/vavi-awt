/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.event;

import java.util.EventListener;


/**
 * エディタのリスナインターフェースです．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.10 010906 nsano be generic <br>
 *          0.11 020503 nsano repackage <br>
 *          0.12 020510 nsano repackage <br>
 *          0.20 020510 nsano deprecate open/close <br>
 */
public interface EditorListener extends EventListener {

    /**
     * エディタがオープンしたとき呼ばれます．
     * @param	ev	エディタイベント
     */
//    void editorOpened(EditorEvent ev);

    /**
     * エディタがアップデートされたとき呼ばれます．
     * @param	ev	エディタイベント
     */
    void editorUpdated(EditorEvent ev);

    /**
     * エディタが終了したとき呼ばれます．
     * @param	ev	エディタイベント
     */
//    void editorClosed(EditorEvent ev);
}

/* */
