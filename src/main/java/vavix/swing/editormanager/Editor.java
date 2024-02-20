/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.swing.editormanager;

import vavi.swing.event.EditorListener;


/**
 * エディタのインターフェースです．
 *
 * TODO add close open exit
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.01 020503 nsano repackage <br>
 */
public interface Editor {

    /**
     * Adds an editor listener.
     *
     * @param l the editor listener
     */
    void addEditorListener(EditorListener l);

    /**
     * Removes an editor listener.
     *
     * @param l the editor listener
     */
    void removeEditorListener(EditorListener l);

    /**
     * Gets the Editor.
     */
    Editor getEditor();

    /**
     * Gets the EditorManager.
     */
    EditorManager getEditorManager();
}

/* */
