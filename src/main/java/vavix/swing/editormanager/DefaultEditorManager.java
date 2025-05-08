/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.swing.editormanager;


/**
 * This is the default implementation class for managing multiple editors.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.10 010906 nsano fix specification??? <br>
 *          0.11 020503 nsano repackage <br>
 */
public class DefaultEditorManager extends EditorManager {

    /**
     * This is the implementation of the process that is called when the editor is opened.
     */
    @Override
    protected void editorOpenedImpl(Editor editor) {}

    /**
     * This is the implementation of the process that is called when the editor is closed.
     */
    @Override
    protected void editorClosedImpl(Editor editor) {}

    /**
     * This is the implementation of the process that is called when the editor is updated.
     */
    @Override
    protected void editorUpdatedImpl(Editor editor) {}

    /**
     * This is the implementation of the process that occurs when an editor is opened using the editor manager.
     */
    @Override
    public void openEditor(Editor editor) {}

    /**
     * This is the implementation of the process that occurs when the editor is closed using the editor manager.
     */
    @Override
    public void closeEditor(Editor editor) {}

    /**
     * This is the implementation of the process that occurs when you update the editor using the editor manager.
     */
    @Override
    public void updateEditor(Editor editor) {}

    /**
     * This is the implementation of the process that occurs when all editors are closed using the editor manager.
     */
    @Override
    protected void closedAllEditorsImpl() {}
}
