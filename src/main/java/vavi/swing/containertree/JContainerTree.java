/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.containertree;

import java.awt.Component;
import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

import vavi.util.Debug;


/**
 * コンテナの JTree
 *
 * @depends ${JDK_HOME}/lib/dt.jar
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020510 nsano initial version <br>
 */
public class JContainerTree extends JTree {

    /** */
//  private Container container;

    /** */
    public JContainerTree(/* Container container*/) {
//      this.container = container;

//      container.addContainerListener(cl);

        setCellRenderer(tcr);
    }

    //-------------------------------------------------------------------------

    /**
     * ツリーのセルレンダラのクラスです．
     * TODO 非表示のとき薄くする
     */
    private DefaultTreeCellRenderer tcr = new DefaultTreeCellRenderer() {
        /** */
        public Component getTreeCellRendererComponent(JTree tree,
                                  Object value,
                                  boolean selected,
                                  boolean expanded,
                                  boolean leaf,
                                  int row,
                                  boolean hasFocus) {

//              String stringValue = tree.convertValueToText(
//                  value, selected, expanded, leaf, row, hasFocus);

                if (! (value instanceof ComponentTreeNode))
                    return this;

                this.hasFocus = hasFocus;

                ComponentTreeNode node = (ComponentTreeNode) value;
                Component component = (Component) node.getUserObject();

                /* Set the text. */
                setText(node.getName());
                /* Tooltips used by the tree. */
                setToolTipText(node.getDescription());

                try {
                    Class<?> beanClass = component.getClass();
//Debug.println(beanClass);
                    BeanInfo info = Introspector.getBeanInfo(beanClass);
                    /* Set the image. */
                    Image image = info.getIcon(BeanInfo.ICON_COLOR_16x16);
                    if (image == null) {
                        setIcon(UIManager.getIcon("jContainerTree.defaultIcon"));
                    } else {
                        setIcon(new ImageIcon(image));
                    }
                } catch (Exception e) {
Debug.println(Level.SEVERE, e);
                }

            if (selected)
            setForeground(getTextSelectionColor());
            else
            setForeground(getTextNonSelectionColor());

            setComponentOrientation(tree.getComponentOrientation());

            /* Update the selected flag for the next paint. */
            this.selected = selected;

            return this;
        }
    };

    static {
        Class<?> clazz = JContainerTree.class;
        UIDefaults table = UIManager.getDefaults();
        table.put("jContainerTree.defaultIcon", LookAndFeel.makeIcon(
            clazz, "/toolbarButtonGraphics/development/Bean16.gif"));
    }
}

/* */
