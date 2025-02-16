/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import vavi.awt.event.SelectionEvent;
import vavi.awt.event.SelectionListener;
import vavi.awt.event.SelectionSupport;
import vavi.util.Debug;


/**
 * SelectionModel.
 *
 * @event SelectionEvent(Container, List<Selectable>)
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020529 nsano initial version <br>
 */
public class SelectionModel {

    /** 選択されていてるオブジェクトのリスト */
    private List<Selectable> selected;

    /** */
    public SelectionModel() {
        selected = new Vector<>();
    }

    /**
     * 選択状態の Selectable のベクタを返します．
     */
    public List<Selectable> getSelected() {
        return selected;
    }

    /**
     * 指定した Selectable のベクタを選択状態にします．
     */
    public void setSelected(List<Selectable> selected) {
Debug.println(Level.INFO, "Warning: be careful to use this method.");
        this.selected = selected;

        fireValueChanged(new SelectionEvent(this, selected));
    }

    /**
     * すべての Selectable を非選択状態にします．
     */
    public void deselectAll() {
        for (Selectable selectable : selected) {
            selectable.setSelected(false);
        }

        selected.clear();

        fireValueChanged(new SelectionEvent(this, selected));
    }

    /**
     * Selectable を選択状態にします．
     *
     * @param selectables
     */
    public void select(Selectable[] selectables) {

        deselectAll();

        for (Selectable selectable : selectables) {
            select(selectable, true);
        }
    }

    /**
     * Selectable を選択状態にします．
     *
     * @param selectable 対象となる Object
     * @param isMultiSelection 複数選択かどうか
     */
    public void select(Selectable selectable, boolean isMultiSelection) {

Debug.println(Level.FINER, selectable);
        boolean isOldSelection = false;

        Iterator<Selectable> i = selected.iterator();
        while (i.hasNext()) {
            Selectable s = i.next();
            if (selectable == s) {
                if (isMultiSelection) {
                    // 新しい選択が，すでに選択済みの場合，
                    // その選択状態を解除する
                    s.setSelected(false);
                    i.remove();
                    isOldSelection = true;
                }
            }
        }

        if (!isOldSelection) {
            // 新しいものが選択されたとき
            if (!isMultiSelection) {
                // すべてを非選択状態に
                for (Selectable value : selected) {
                    value.setSelected(false);
                }
                selected.clear();
            }
            selected.add(selectable);

            // 選択されているすべてを選択状態にする
            for (Selectable value : selected) {
                value.setSelected(true);
            }
        }

        fireValueChanged(new SelectionEvent(this, selected));
    }

    // -------------------------------------------------------------------------

    /** SelectionEvent 機構のユーティリティ */
    private final SelectionSupport ss = new SelectionSupport();

    /** Selection リスナーをアタッチします． */
    public void addSelectionListener(SelectionListener l) {
        ss.addSelectionListener(l);
    }

    /** Selection リスナーをリムーブします． */
    public void removeSelectionListener(SelectionListener l) {
        ss.removeSelectionListener(l);
    }

    /** */
    protected void fireValueChanged(SelectionEvent ev) {
        ss.fireValueChanged(ev);
    }
}
