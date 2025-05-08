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
 * The drop-down list is a combo box that displays a tree.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020330 nsano rename, repackage <br>
 */
public class JTreeComboBox extends JComboBox<Object> {

    /** Creates a combo box whose drop-down list displays a tree. */
    public JTreeComboBox(TreeModel aTreeModel) {
        super();
        setModel(new TreeToListModel(aTreeModel));
        setRenderer(new DefaultTreeComboBoxCellRenderer<>());
    }

    /** This class converts a tree model into a list model and handles it. */
    static class TreeToListModel extends AbstractListModel<Object> implements ComboBoxModel<Object>, TreeModelListener {

        final TreeModel source;

        boolean invalid = true;

        Object currentValue;

        List<ListEntry> cache = new ArrayList<>();

        /** */
        public TreeToListModel(TreeModel aTreeModel) {
            source = aTreeModel;
            aTreeModel.addTreeModelListener(this);
            // setRenderer(new ListEntryRenderer());
        }

        @Override
        public void setSelectedItem(Object anObject) {
            currentValue = anObject;
            fireContentsChanged(this, -1, -1);
        }

        @Override
        public Object getSelectedItem() {
            return currentValue;
        }

        @Override
        public int getSize() {
            validate();
            return cache.size();
        }

        @Override
        public Object getElementAt(int index) {
            return cache.get(index).object;
        }

        @Override
        public void treeNodesChanged(TreeModelEvent ev) {
            invalid = true;
        }

        @Override
        public void treeNodesInserted(TreeModelEvent ev) {
            invalid = true;
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent ev) {
            invalid = true;
        }

        @Override
        public void treeStructureChanged(TreeModelEvent ev) {
            invalid = true;
        }

        /** */
        void validate() {
            if (invalid) {
                cache = new Vector<>();
                cacheTree(source.getRoot(), 0);
                if (!cache.isEmpty()) {
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
 * This class represents one entry in a JTreeComboBox.
 */
class ListEntry {

    /** Entry object */
    final Object object;

    /** Tree Hierarchy */
    final int level;

    /** Is it the end of the line? */
    final boolean isNode;

    /** Constructs an entry for a JTreeComboBox. */
    public ListEntry(Object anObject, int aLevel, boolean isNode) {
        object = anObject;
        level = aLevel;
        this.isNode = isNode;
    }

    /** Returns the entry object. */
    public Object object() {
        return object;
    }

    /** Returns the tree hierarchy of entries. */
    public int level() {
        return level;
    }

    /** Returns whether it is a last leaf. */
    public boolean isNode() {
        return isNode;
    }
}
