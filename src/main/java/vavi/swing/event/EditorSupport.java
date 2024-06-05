/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Editor 機構のの基本実装クラスです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.10 020503 nsano use EventListenerList <br>
 *          0.11 020503 nsano repackage <br>
 *          0.12 020510 nsano repackage <br>
 *          0.20 020510 nsano deprecate open/close <br>
 */
public class EditorSupport implements Serializable {

    /** The editor listeners */
    private final List<EditorListener> listeners = new ArrayList<>();

    /** EditorListener を追加します． */
    public void addEditorListener(EditorListener l) {
        listeners.add(l);
    }

    /** EditorListener を削除します． */
    public void removeEditorListener(EditorListener l) {
        listeners.remove(l);
    }

    /** エディタがアップデートされたイベントを発行します． */
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void fireEditorUpdated(EditorEvent ev) {
        // TODO enhanced for causes java.util.ConcurrentModificationException
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).editorUpdated(ev);
        }
    }
}
