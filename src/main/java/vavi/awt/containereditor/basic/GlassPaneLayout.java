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

import vavi.util.Debug;


/**
 * GlassPaneLayout
 *
 * @author    <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version    0.00    020613    nsano    initial version <br>
 */
public class GlassPaneLayout implements LayoutManager {

    /**
     * このクラスでは使用しません。
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * 指定されたパネルにコンテナを配置します。
     */
    public void layoutContainer(Container parent) {
//Debug.println(Debug.getCallerMethod());
//Debug.println(parent.getPreferredSize().width + ", " + parent.getPreferredSize().height);
//Debug.println(parent);
//Debug.println(parent.getSize().width + ", " + parent.getSize().height);
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
    public Dimension minimumLayoutSize(Container parent) {
Debug.println(parent.getSize().width + ", " + parent.getSize().height);
        return parent.getSize();
    }

    /**
     * 指定された親コンテナにコンポーネントを配置した時の
     * パネルの推奨サイズを計算します。
     */
    public Dimension preferredLayoutSize(Container parent) {
Debug.println(parent.getSize().width + ", " + parent.getSize().height);
        return parent.getSize();
    }

    /**
     * このクラスでは使用しません。
     */
    public void removeLayoutComponent(Component comp) {
    }
}

/* */
