/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt;

import java.util.List;
import java.util.Vector;

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
Debug.println("Warnning: be careful to use this method.");
        this.selected = selected;

        fireValueChanged(new SelectionEvent(this, selected));
    }

    /**
     * すべての Selectable を非選択状態にします．
     */
    public void deselectAll() {
        for (int i = 0; i < selected.size(); i++) {
            selected.get(i).setSelected(false);
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

        for (int i = 0; i < selectables.length; i++) {
            select(selectables[i], true);
        }
    }

    /**
     * Selectable を選択状態にします．
     *
     * @param selectable 対象となる Object
     * @param isMultiSelection 複数選択かどうか
     */
    public void select(Selectable selectable, boolean isMultiSelection) {

        // Debug.println(selectable);
        boolean isOldSelection = false;

        for (int i = 0; i < selected.size(); i++) {
            if (selectable == selected.get(i)) {
                if (isMultiSelection) {
                    // 新しい選択が，すでに選択済みの場合，
                    // その選択状態を解除する
                    selected.get(i).setSelected(false);
                    selected.remove(i);
                    isOldSelection = true;
                }
            }
        }

        if (!isOldSelection) {
            // 新しいものが選択されたとき
            if (!isMultiSelection) {
                // すべてを非選択状態に
                for (int i = 0; i < selected.size(); i++) {
                    selected.get(i).setSelected(false);
                }
                selected.clear();
            }
            selected.add(selectable);

            // 選択されているすべてを選択状態にする
            for (int i = 0; i < selected.size(); i++) {
                selected.get(i).setSelected(true);
            }
        }

        fireValueChanged(new SelectionEvent(this, selected));
    }

    // -------------------------------------------------------------------------

    /** SelectionEvent 機構のユーティリティ */
    private SelectionSupport ss = new SelectionSupport();

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

/* */
