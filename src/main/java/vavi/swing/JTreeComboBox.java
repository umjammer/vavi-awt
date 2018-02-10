/*
 * @(#)TreeCombo.java    1.6 98/08/26
 *
 * Copyright 1997, 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package vavi.swing;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;


/**
 * ドロップダウンリストがツリー表示のコンボボックスです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020330 nsano rename, repackage <br>
 */
public class JTreeComboBox extends JComboBox<Object> {

    /** ドロップダウンリストがツリー表示のコンボボックスを構築します． */
    public JTreeComboBox(TreeModel aTreeModel) {
        super();
        setModel(new TreeToListModel(aTreeModel));
        setRenderer(new DefaultTreeComboBoxCellRenderer<>());
    }

    /** ツリーモデルをリストモデルに変換して扱うクラスです． */
    class TreeToListModel extends AbstractListModel<Object> implements ComboBoxModel<Object>, TreeModelListener {

        TreeModel source;

        boolean invalid = true;

        Object currentValue;

        List<ListEntry> cache = new ArrayList<>();

        /** */
        public TreeToListModel(TreeModel aTreeModel) {
            source = aTreeModel;
            aTreeModel.addTreeModelListener(this);
            // setRenderer(new ListEntryRenderer());
        }

        /** */
        public void setSelectedItem(Object anObject) {
            currentValue = anObject;
            fireContentsChanged(this, -1, -1);
        }

        /** */
        public Object getSelectedItem() {
            return currentValue;
        }

        /** */
        public int getSize() {
            validate();
            return cache.size();
        }

        /** */
        public Object getElementAt(int index) {
            return cache.get(index).object;
        }

        /** */
        public void treeNodesChanged(TreeModelEvent ev) {
            invalid = true;
        }

        /** */
        public void treeNodesInserted(TreeModelEvent ev) {
            invalid = true;
        }

        /** */
        public void treeNodesRemoved(TreeModelEvent ev) {
            invalid = true;
        }

        /** */
        public void treeStructureChanged(TreeModelEvent ev) {
            invalid = true;
        }

        /** */
        void validate() {
            if (invalid) {
                cache = new Vector<>();
                cacheTree(source.getRoot(), 0);
                if (cache.size() > 0) {
                    currentValue = cache.get(0);
                }
                invalid = false;
                fireContentsChanged(this, 0, 0);
            }
        }

        /** */
        void cacheTree(Object anObject, int level) {
            if (source.isLeaf(anObject)) {
                addListEntry(anObject, level, false);
            } else {
                int c = source.getChildCount(anObject);
                int i;
                Object child;

                addListEntry(anObject, level, true);
                level++;

                for (i = 0; i < c; i++) {
                    child = source.getChild(anObject, i);
                    cacheTree(child, level);
                }

                level--;
            }
        }

        /** */
        void addListEntry(Object anObject, int level, boolean isNode) {
            cache.add(new ListEntry(anObject, level, isNode));
        }
    }
}

/**
 * JTreeComboBox の一エントリを表すクラスです．
 */
class ListEntry {

    /** エントリのオブジェクト */
    Object object;

    /** ツリーの階層 */
    int level;

    /** 末葉かどうか */
    boolean isNode;

    /** JTreeComboBox の一エントリを構築します． */
    public ListEntry(Object anObject, int aLevel, boolean isNode) {
        object = anObject;
        level = aLevel;
        this.isNode = isNode;
    }

    /** エントリのオブジェクトを返します． */
    public Object object() {
        return object;
    }

    /** エントリのツリーの階層を返します． */
    public int level() {
        return level;
    }

    /** 末葉かどうかを返します． */
    public boolean isNode() {
        return isNode;
    }
}

/* */
