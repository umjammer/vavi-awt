/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.event;

import java.io.Serializable;

import javax.swing.event.EventListenerList;


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
public class EditorSupport
    implements Serializable {

    /** The editor listeners */
    private EventListenerList listenerList = new EventListenerList();

    /** EditorListener を追加します． */
    public void addEditorListener(EditorListener l) {
        listenerList.add(EditorListener.class, l);
    }

    /** EditorListener を削除します． */
    public void removeEditorListener(EditorListener l) {
        listenerList.remove(EditorListener.class, l);
    }

    /** エディタがオープンされたイベントを発行します． */
//      public void fireEditorOpened(EditorEvent ev) {
//          Object[] listeners = listenerList.getListenerList();
//          for (int i = listeners.length - 2; i >= 0; i -= 2) {
//              if (listeners[i] == EditorListener.class) {
//                  ((EditorListener) listeners[i + 1]).editorOpened(ev);
//              }
//          }
//      }

    /** エディタがアップデートされたイベントを発行します． */
    public void fireEditorUpdated(EditorEvent ev) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == EditorListener.class) {
                ((EditorListener) listeners[i + 1]).editorUpdated(ev);
            }
        }
    }

    /** エディタがクローズされたイベントを発行します． */
//      public void fireEditorClosed(EditorEvent ev) {
//          Object[] listeners = listenerList.getListenerList();
//          for (int i = listeners.length - 2; i >= 0; i -= 2) {
//              if (listeners[i] == EditorListener.class) {
//                  ((EditorListener) listeners[i + 1]).editorClosed(ev);
//              }
//          }
//      }
}

/* */
