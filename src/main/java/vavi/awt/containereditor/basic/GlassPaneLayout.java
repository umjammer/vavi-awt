/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.containereditor.basic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * GlassPaneLayout
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020613 nsano initial version <br>
 */
public class GlassPaneLayout implements LayoutManager {

    /**
     * this class doesn't use this method.
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * 指定されたパネルにコンテナを配置します。
     */
    @Override
    public void layoutContainer(Container parent) {
Debug.println(Level.FINER, Debug.getCallerMethod());
Debug.println(Level.FINER, parent.getPreferredSize().width + ", " + parent.getPreferredSize().height);
Debug.println(Level.FINER, parent);
Debug.println(Level.FINER, parent.getSize().width + ", " + parent.getSize().height);
        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component component = parent.getComponent(i);
            component.setLocation(new Point(0, 0));
            component.setSize(parent.getSize());
        }
    }

    /**
     * 指定された親コンテナにコンポーネントを配置した時の
     * パネルの最小サイズを計算します。
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
Debug.println(Level.FINE, parent.getSize().width + ", " + parent.getSize().height);
        return parent.getSize();
    }

    /**
     * 指定された親コンテナにコンポーネントを配置した時の
     * パネルの推奨サイズを計算します。
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
Debug.println(Level.FINE, parent.getSize().width + ", " + parent.getSize().height);
        return parent.getSize();
    }

    /**
     * this class doesn't use this method.
     */
    @Override
    public void removeLayoutComponent(Component comp) {
    }
}
