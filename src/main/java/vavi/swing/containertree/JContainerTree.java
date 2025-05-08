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
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

import static java.lang.System.getLogger;


/**
 * Container JTree
 *
 * @depends ${JDK_HOME}/lib/dt.jar
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020510 nsano initial version <br>
 */
public class JContainerTree extends JTree {

    private static final Logger logger = getLogger(JContainerTree.class.getName());

//    /** */
//    private Container container;

    /** */
    public JContainerTree(/* Container container*/) {
//      this.container = container;

//      container.addContainerListener(cl);

        setCellRenderer(tcr);
    }

    // ----

    /**
     * Container JTree
     * TODO Make it lighter when hidden
     */
    private final DefaultTreeCellRenderer tcr = new DefaultTreeCellRenderer() {
        /** */
        @Override
        public Component getTreeCellRendererComponent(JTree tree,
                                                      Object value,
                                                      boolean selected,
                                                      boolean expanded,
                                                      boolean leaf,
                                                      int row,
                                                      boolean hasFocus) {

//              String stringValue = tree.convertValueToText(
//                  value, selected, expanded, leaf, row, hasFocus);

                if (! (value instanceof ComponentTreeNode node))
                    return this;

                this.hasFocus = hasFocus;

            Component component = (Component) node.getUserObject();

                /* Set the text. */
                setText(node.getName());
                /* Tooltips used by the tree. */
                setToolTipText(node.getDescription());

                try {
                    Class<?> beanClass = component.getClass();
logger.log(Level.TRACE, beanClass);
                    BeanInfo info = Introspector.getBeanInfo(beanClass);
                    /* Set the image. */
                    Image image = info.getIcon(BeanInfo.ICON_COLOR_16x16);
                    if (image == null) {
                        setIcon(UIManager.getIcon("jContainerTree.defaultIcon"));
                    } else {
                        setIcon(new ImageIcon(image));
                    }
                } catch (Exception e) {
logger.log(Level.ERROR, e.getMessage(), e);
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
