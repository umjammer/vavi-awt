/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;


/**
 * FlowLayoutCustomizer.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020527 nsano initial version <br>
 *          0.01 020618 nsano refine <br>
 */
public class FlowLayoutCustomizer extends BasicLayoutManagerCustomizer {

    /** layout for layoutPanel */
    private FlowLayout flow;

    /** */
    public FlowLayoutCustomizer() {
        // UI
        lcPanel.setVisible(false);

        // init
        this.flow = new FlowLayout();
        layoutPanel.setLayout(flow);

        this.layout = new FlowLayout();
        tableModel.setObject(layout);
        tableModel.fireTableDataChanged();

        tableModel.addTableModelListener(tml);
    }

    /** */
    private TableModelListener tml = new TableModelListener() {
        public void tableChanged(TableModelEvent ev) {
            updateLayout();
        }
    };

    /** */
    private void updateLayout() {
        flow.setHgap(Math.round(((FlowLayout) layout).getHgap() * ratio));
        flow.setVgap(Math.round(((FlowLayout) layout).getVgap() * ratio));
        flow.setAlignment(((FlowLayout) layout).getAlignment());

        flow.layoutContainer(layoutPanel);
    }

    /** */
    public void setObject(LayoutManager layout) {
        updateLayout();

//Debug.println("flow  : " + flow.hashCode());
//Debug.println("layout: " + layout.hashCode());
        super.setObject(layout);

        tableModel.setObject(layout);
        tableModel.fireTableDataChanged();
    }

    /** TODO */
    public LayoutManager getObject() {
        layoutPanel.validate();
        layoutPanel.repaint();
        return super.getObject();
    }

    /** */
    public void setContainer(Container container) {
        super.setContainer(container);

        layoutPanel.removeAll();
        components.clear();

        for (int i = 0; i < container.getComponentCount(); i++) {
            Component component = container.getComponent(i);
//  Component c = component;
//  Debug.println("---- " + i + " ----");
//  Debug.println(c.getSize().width+", "+c.getSize().height);
//  Debug.println(c.getMinimumSize().width+", "+c.getMinimumSize().height);
//  Debug.println(c.getPreferredSize().width+", "+c.getPreferredSize().height);
//  Debug.println(c.getMaximumSize().width+", "+c.getMaximumSize().height);

            JLabel controller = new JLabel("" + i);
            controller.setHorizontalAlignment(JLabel.CENTER);
            controller.setOpaque(true);
            controller.setBorder(BorderFactory.createRaisedBevelBorder());
            Dimension size = component.getPreferredSize();
            controller.setPreferredSize(ajustRatio(size));
//  c = controller;
//  Debug.println("----");
//  Debug.println(c.getSize().width+", "+c.getSize().height);
//  Debug.println(c.getMinimumSize().width+", "+c.getMinimumSize().height);
//  Debug.println(c.getPreferredSize().width+", "+c.getPreferredSize().height);
//  Debug.println(c.getMaximumSize().width+", "+c.getMaximumSize().height);

            layoutPanel.add(controller);

            components.put(controller, component);
        }

        layoutPanel.validate();
        layoutPanel.repaint();
    }

    /** */
    private Dimension ajustRatio(Dimension size) {
        Dimension newSize = new Dimension();
        int w = Math.round(size.width  * ratio);
        int h = Math.round(size.height * ratio);
        newSize.width  = w > 32767 ? 32767 : w;
        newSize.height = h > 32767 ? 32767 : h;
        return newSize;
    }
}

/* */
