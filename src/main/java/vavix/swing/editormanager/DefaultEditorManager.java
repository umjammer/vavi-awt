/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.swing.editormanager;


/**
 * 複数のエディタを管理するクラスのデフォルトの実装クラスです．
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.10 010906 nsano fix specification??? <br>
 *          0.11 020503 nsano repackage <br>
 */
public class DefaultEditorManager extends EditorManager {

    /**
     * エディタがオープンされたときに呼ばれる処理の実装です．
     */
    protected void editorOpenedImpl(Editor editor) {}

    /**
     * エディタがクローズされたときに呼ばれる処理の実装です．
     */
    protected void editorClosedImpl(Editor editor) {}

    /**
     * エディタがアップデートされたときに呼ばれる処理の実装です．
     */
    protected void editorUpdatedImpl(Editor editor) {}

    /**
     * エディタマネージャを使ってエディタをオープンした時の処理の実装です．
     */
    public void openEditor(Editor editor) {}

    /**
     * エディタマネージャを使ってエディタをクローズした時の処理の実装です．
     */
    public void closeEditor(Editor editor) {}

    /**
     * エディタマネージャを使ってエディタをアップデートした時の処理の実装です．
     */
    public void updateEditor(Editor editor) {}

    /**
     * エディタマネージャを使って全エディタをクローズした時の処理の実装です．
     */
    protected void closedAllEditorsImpl() {}
}

/* */
