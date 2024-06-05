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
 * 複数のエディタを管理する基本クラスです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.10 010906 nsano be abstract class <br>
 *          0.11 020503 nsano repackage <br>
 */
public abstract class EditorManager implements EditorListener {

    /** 複数のエディタ管理用ベクタ */
    protected final List<Editor> editors = new ArrayList<>();

    /** カレントのエディタ */
    protected Editor current;

    /**
     * エディタがオープンされたときに呼ばれます．
     */
    public void editorOpened(EditorEvent ev) {
        Editor editor = (Editor) ev.getSource();
        editors.add(editor);
        current = editor;

        editorOpenedImpl(editor);
    }

    /**
     * エディタがオープンされたときに呼ばれる処理の実装です．
     */
    protected abstract void editorOpenedImpl(Editor editor);

    /**
     * エディタがクローズされたときに呼ばれます．
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
     * エディタがクローズされたときに呼ばれる処理の実装です．
     */
    protected abstract void editorClosedImpl(Editor editor);

    /**
     * エディタがアップデートされたときに呼ばれます．
     */
    @Override
    public void editorUpdated(EditorEvent ev) {
        Editor editor = (Editor) ev.getSource();
        editorUpdatedImpl(editor);
    }

    /**
     * エディタがアップデートされたときに呼ばれる処理の実装です．
     */
    protected abstract void editorUpdatedImpl(Editor editor);

    /**
     * エディタをオープンします．
     *
     * @param editor エディタ
     */
    public abstract void openEditor(Editor editor);

    /**
     * エディタをクローズします．
     *
     * @param editor エディタ
     */
    public abstract void closeEditor(Editor editor);

    /**
     * エディタをアップデートします．
     *
     * @param editor エディタ
     */
    public abstract void updateEditor(Editor editor);

    /**
     * 全エディタをクローズします．
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
     * エディタマネージャを使って全エディタをクローズしたときの処理の実装です．
     */
    protected abstract void closedAllEditorsImpl();

    /**
     * エディタのリストを取得します．
     *
     * @return エディタのリスト
     */
    public List<Editor> getEditors() {
        return editors;
    }

    /**
     * カレントのエディタを取得します．
     */
    public Editor getCurrentEditor() {
        return current;
    }
}
