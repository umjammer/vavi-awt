/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.swing.editormanager;

import java.util.ArrayList;
import java.util.List;

import vavi.swing.event.EditorEvent;
import vavi.swing.event.EditorListener;


/**
 * A base class for managing multiple editors.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.10 010906 nsano be abstract class <br>
 *          0.11 020503 nsano repackage <br>
 */
public abstract class EditorManager implements EditorListener {

    /** Multiple editor management list */
    protected final List<Editor> editors = new ArrayList<>();

    /** The current editor */
    protected Editor current;

    /**
     * Called when the editor is opened.
     */
    public void editorOpened(EditorEvent ev) {
        Editor editor = (Editor) ev.getSource();
        editors.add(editor);
        current = editor;

        editorOpenedImpl(editor);
    }

    /**
     * This is the implementation of the process that is called when the editor is opened.
     */
    protected abstract void editorOpenedImpl(Editor editor);

    /**
     * Called when the editor is closed.
     */
    public void editorClosed(EditorEvent ev) {
        Editor editor = (Editor) ev.getSource();
        editors.remove(editor);

        int size = editors.size();
        if (size == 0) {
            current = null;
        } else {
            current = editors.get(size - 1);
        }

        editorClosedImpl(editor);
    }

    /**
     * This is the implementation of the process that is called when the editor is closed.
     */
    protected abstract void editorClosedImpl(Editor editor);

    @Override
    public void editorUpdated(EditorEvent ev) {
        Editor editor = (Editor) ev.getSource();
        editorUpdatedImpl(editor);
    }

    /**
     * This is the implementation of the process that is called when the editor is updated.
     */
    protected abstract void editorUpdatedImpl(Editor editor);

    /**
     * Open the editor.
     *
     * @param editor editor
     */
    public abstract void openEditor(Editor editor);

    /**
     * Close the editor.
     *
     * @param editor editor
     */
    public abstract void closeEditor(Editor editor);

    /**
     * Update the editor.
     *
     * @param editor editor
     */
    public abstract void updateEditor(Editor editor);

    /**
     * Close all editors.
     */
    public void closeAllEditors() {
        @SuppressWarnings("unchecked")
        List<Editor> clones = (List<Editor>) ((ArrayList<Editor>) editors).clone();

        for (Editor editor : clones) {
            closeEditor(editor);
        }

        closedAllEditorsImpl();
    }

    /**
     * This is the implementation of the process that occurs when all editors are closed using the editor manager.
     */
    protected abstract void closedAllEditorsImpl();

    /**
     * Gets the list of editors.
     *
     * @return List of Editors
     */
    public List<Editor> getEditors() {
        return editors;
    }

    /**
     * Gets the current editor.
     */
    public Editor getCurrentEditor() {
        return current;
    }
}
