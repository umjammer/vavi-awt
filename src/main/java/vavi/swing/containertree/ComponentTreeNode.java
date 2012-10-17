/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.containertree;

import java.awt.Component;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 * コンテナツリー上のノードです．
 *
 * @author	<a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version	0.00	020510	nsano	initial version <br>
 */
public class ComponentTreeNode extends DefaultMutableTreeNode {

    /**
     * コンポーネントを表すツリーノードを構築します．
     * @param component Component
     */
    public ComponentTreeNode(Component component) {
        this.userObject = component;
    }

    /**
     * このノードを表す文字列を返します．
     */
    public String getName() {
        return toString();
    }

    /**
     * このノードを表す文字列を返します．
     */
    public String getDescription() {
        return toString();
    }

    /**
     * このノードを表す文字列を返します．
     */
    public String toString() {
        return ((Component) userObject).getName();
    }
}

/* */
