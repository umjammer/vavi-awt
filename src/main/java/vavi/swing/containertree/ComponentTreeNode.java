/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.containertree;

import java.awt.Component;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 * A node on a container tree.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020510 nsano initial version <br>
 */
public class ComponentTreeNode extends DefaultMutableTreeNode {

    /**
     * Constructs a tree node to represent the component.
     * @param component Component
     */
    public ComponentTreeNode(Component component) {
        this.userObject = component;
    }

    /**
     * Returns a string representing this node.
     */
    public String getName() {
        return toString();
    }

    /**
     * Returns a string representing this node.
     */
    public String getDescription() {
        return toString();
    }

    /**
     * Returns a string representing this node.
     */
    public String toString() {
        return ((Component) userObject).getName();
    }
}
